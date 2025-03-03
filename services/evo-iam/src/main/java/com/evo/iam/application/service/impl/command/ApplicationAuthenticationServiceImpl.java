package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.SignInRequest;
import com.evo.iam.application.dto.request.SignOutRequest;
import com.evo.iam.application.dto.request.SignUpRequest;
import com.evo.iam.application.dto.request.VerificationRequest;
import com.evo.iam.application.dto.request.ActivateAccountRequest;
import com.evo.iam.application.dto.response.AuthenticationResponse;
import com.evo.iam.application.mapper.RoleDomainMapper;
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
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.infrastructure.persistence.mapper.RoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.iam.application.security.JwtUtil;
import com.evo.iam.infrastructure.adapter.tfa.impl.TfaServiceImpl;
import com.evo.security.impl.BlacklistedTokenService;
import com.evo.util.IdUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationAuthenticationServiceImpl implements AuthenticationCmdService {
    private final JwtUtil jwtUtil;
    private final TfaServiceImpl tfaService;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokenService blacklistedTokenService;


    private final UserDomainMapper userDomainMapper;
    private final UserEntityMapperImpl userEntityMapper;
    private final UserRoleDomainMapper userRoleDomainMapper;
    private final RoleEntityMapperImpl roleEntityMapper;

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
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }

        try {
            if (user.getTfaEnabled()) {
                return AuthenticationResponse.builder()
                        .tfaRequired(true)
                        .build();
            } else {
                String accessToken = jwtUtil.generateAccessToken(user);
                String refreshToken = jwtUtil.generateRefreshToken(user);

                user.logActivity(userActivityDomainMapper.toUserActivityCmd(request.getEmail(),
                                "USER SIGN IN"), httpRequest);

                userDomainRepository.save(user);

                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tfaRequired(false)
                        .build();
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String signUp(SignUpRequest request, HttpServletRequest httpRequest) {
        if (userEntityRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try {
            UserRegistrationCmd cmd = userDomainMapper.signUpFrom(request);
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
            user.logActivity(userActivityDomainMapper.toUserActivityCmd(request.getEmail(),
                    "USER SIGN UP"), httpRequest);

            userDomainRepository.save(user);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return "Sign up successfully! Welcome, " + request.getLastname()
                + " " + request.getFirstname() + "!";
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

    @Override
    public AuthenticationResponse verification(VerificationRequest request, HttpServletRequest httpRequest) {
        User user = findUser(request.getEmail());

        if (!tfaService.verifyCode(user.getSecretKey(), request.getOtp())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
        if (user.getVerified()) {
            throw new AppException(ErrorCode.ALREADY_VERIFIED);
        }

        try {
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            user.markAsVerified();
            user.logActivity(userActivityDomainMapper.toUserActivityCmd(request.getEmail(),
                    "USER VERIFY"), httpRequest);
            userDomainRepository.save(user);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tfaRequired(false)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        String email;
        try {
            email = jwtUtil.extractEmail(refreshToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = findUser(email);
        if (blacklistedTokenService.isTokenBlacklisted(refreshToken)) {
            throw new AppException(ErrorCode.BLACKLISTED_REFRESH_TOKEN);
        }

        try {
            String accessToken = jwtUtil.generateAccessToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String logout(SignOutRequest request, HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        if (blacklistedTokenService.isTokenBlacklisted(request.getRefreshToken())) {
            throw new AppException(ErrorCode.BLACKLISTED_REFRESH_TOKEN);
        }
        String accessToken = authHeader.substring(7);
        String email = jwtUtil.extractEmail(accessToken);
        User user = findUser(email);
        user.markAsUnverified();
        user.logActivity(userActivityDomainMapper.toUserActivityCmd(email,
                "USER SIGN OUT"), httpRequest);
        userDomainRepository.save(user);

        Date accessTokenExpiration = jwtUtil.getSystemJwtExpirationTime(accessToken);
        long accessTokenExpirationDuration = accessTokenExpiration.getTime() - System.currentTimeMillis();
        blacklistedTokenService.blacklistedAccessToken(accessToken, accessTokenExpirationDuration);

        Date refreshTokenExpiration = jwtUtil.getSystemJwtExpirationTime(request.getRefreshToken());
        long refreshTokenExpirationDuration = refreshTokenExpiration.getTime() - System.currentTimeMillis();
        blacklistedTokenService.blacklistedRefreshToken(request.getRefreshToken(), refreshTokenExpirationDuration);

        return "Log out successfully, See you later!";
    }

    private User findUser(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userEntityMapper.toDomain(userEntity);
    }
}
