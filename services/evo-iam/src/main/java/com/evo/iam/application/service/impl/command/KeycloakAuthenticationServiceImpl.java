package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.SignInRequest;
import com.evo.iam.application.dto.request.SignOutRequest;
import com.evo.iam.application.dto.request.SignUpRequest;
import com.evo.iam.application.dto.request.VerificationRequest;
import com.evo.iam.application.dto.request.ActivateAccountRequest;
import com.evo.iam.application.dto.response.AuthenticationResponse;
import com.evo.iam.application.mapper.UserActivityDomainMapper;
import com.evo.iam.application.mapper.UserRoleDomainMapper;
import com.evo.iam.application.service.AuthenticationCmdService;
import com.evo.iam.domain.Role;
import com.evo.iam.domain.User;
import com.evo.iam.domain.command.ActivateAccountCmd;
import com.evo.iam.domain.command.UserCreationCmd;
import com.evo.iam.domain.command.UserRegistrationCmd;
import com.evo.iam.domain.command.UserRoleCmd;
import com.evo.iam.domain.repository.RoleDomainRepository;
import com.evo.iam.domain.repository.UserDomainRepository;
import com.evo.iam.infrastructure.adapter.keycloak.KeycloakUtils;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.infrastructure.persistence.mapper.RoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.iam.infrastructure.adapter.keycloak.KeycloakProperties;
import com.evo.iam.infrastructure.adapter.tfa.impl.TfaServiceImpl;
import com.evo.util.IdUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAuthenticationServiceImpl implements AuthenticationCmdService {
    private final KeycloakUtils keycloakUtils;
    private final RestTemplate restTemplate;
    private final TfaServiceImpl tfaService;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakProperties keycloakProperties;

    private final RoleEntityMapperImpl roleEntityMapper;
    private final UserDomainMapper userDomainMapper;
    private final UserEntityMapperImpl userEntityMapper;
    private final UserRoleDomainMapper userRoleDomainMapper;

    private final UserDomainRepository userDomainRepository;
    private final UserEntityRepository userEntityRepository;
    private final RoleDomainRepository roleDomainRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final UserActivityDomainMapper userActivityDomainMapper;

    @Override
    public AuthenticationResponse signIn(SignInRequest request, HttpServletRequest httpRequest) {
        User user = findUser(request.getEmail());

        if (user.getDeleted()) {
            throw new AppException(ErrorCode.USER_DEACTIVATED);
        }
        if (!user.getStatus().equals("activated")) {
            throw new AppException(ErrorCode.INCOMPLETE_PROFILE);
        }

        Map<String, Object> responseBody = keycloakUtils.keycloakLogin(request);
        String accessToken = (String) responseBody.get("access_token");
        String refreshToken = (String) responseBody.get("refresh_token");

        try {
            user.logActivity(userActivityDomainMapper.toUserActivityCmd(request.getEmail(),
                    "USER SIGN IN"), httpRequest);
            userDomainRepository.save(user);

            return AuthenticationResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tfaRequired(false)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String signUp(SignUpRequest request, HttpServletRequest httpRequest) {
        if (userEntityRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserRegistrationCmd cmd = userDomainMapper.signUpFrom(request);
        keycloakUtils.createKeycloakUser(cmd);
        cmd.setPassword(passwordEncoder.encode(request.getPassword()));
        cmd.setSecretKey(tfaService.generateSecretKey());

        Role role = roleEntityRepository.findByName("USER")
                .map(roleEntityMapper::toDomain)
                .orElseGet(() -> roleDomainRepository.save(
                        Role.builder()
                                .id(IdUtils.nextId())
                                .name("USER")
                                .root(false)
                                .deleted(false)
                                .build()
                ));

        cmd.setUserRoleCmd(userRoleDomainMapper.toUserRoleCmd(role));
        User user = new User(cmd);

        user.logActivity(userActivityDomainMapper.toUserActivityCmd(
                request.getEmail(), "USER SIGN UP"), httpRequest);

        userDomainRepository.save(user);

        return "Sign-up successful! Welcome, " + request.getLastname() + " " + request.getFirstname() + "!";
    }



    @Override
    public String activateAccount(String email, ActivateAccountRequest request, HttpServletRequest httpRequest) {
        User user = findUser(email);
        ActivateAccountCmd cmd = new ActivateAccountCmd();
        userDomainMapper.activateFrom(cmd, request);
        user.activate(cmd);

        user.logActivity(userActivityDomainMapper.toUserActivityCmd(email,
                "ACCOUNT ACTIVATE"), httpRequest);
        userDomainRepository.save(user);

        return "Your account has been successfully activated!";
    }

    //in development
    @Override
    public AuthenticationResponse verification(VerificationRequest verificationRequest, HttpServletRequest httpRequest) {
        UserEntity userEntity = userEntityRepository.findByEmail(verificationRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!tfaService.verifyCode(userEntity.getSecretKey(), verificationRequest.getOtp())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        if (userEntity.getVerified()) {
            throw new AppException(ErrorCode.ALREADY_VERIFIED);
        }

        return null;
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        Map<String, String> responseBody = keycloakUtils.refreshToken(refreshToken);

        return AuthenticationResponse
                .builder()
                .accessToken(responseBody.get("access_token"))
                .refreshToken(responseBody.get("refresh_token"))
                .build();
    }

    @Override
    public String logout(SignOutRequest request, HttpServletRequest httpRequest) {
        return "";
    }

//    @Override
//    public String logout(SignOutRequest signOutRequest, HttpServletRequest request) {
//        try {
//            revokeRefreshToken(signOutRequest.getRefreshToken());
//
//            String accessToken = request.getHeader("Authorization").substring("Bearer ".length());
//            revokeAccessToken(accessToken);
//
//            return "Logout successful! See you later!";
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
//                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
//            } else {
//                throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
//            }
//        }
//    }
//
//    private void revokeRefreshToken(String refreshToken) {
//        String logoutUrl = keycloakProperties.getAuthServerUrl()
//                + "/realms/"
//                + keycloakProperties.getRealm()
//                + "/protocol/openid-connect/logout";
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", keycloakProperties.getClientId());
//        body.add("client_secret", keycloakProperties.getClientSecret());
//        body.add("refresh_token", refreshToken);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
//
//        restTemplate.exchange(
//                logoutUrl,
//                HttpMethod.POST,
//                requestEntity,
//                Void.class
//        );
//    }

    private void revokeAccessToken(String accessToken) {
        String revokeAccessTokenUrl = keycloakProperties.getAuthServerUrl()
                + "/realms/"
                + keycloakProperties.getRealm()
                + "/protocol/openid-connect/revoke";

        MultiValueMap<String, String> revokeBody = new LinkedMultiValueMap<>();
        revokeBody.add("client_id", keycloakProperties.getClientId());
        revokeBody.add("client_secret", keycloakProperties.getClientSecret());
        revokeBody.add("token", accessToken);  // Access token to revoke

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> revokeRequestEntity = new HttpEntity<>(revokeBody, headers);

        restTemplate.exchange(
                revokeAccessTokenUrl,
                HttpMethod.POST,
                revokeRequestEntity,
                Void.class
        );
    }

    private User findUser(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userEntityMapper.toDomain(userEntity);
    }
}
