package com.evo.iam.infrastructure.adapter.googleSSO.impl;

import com.evo.iam.application.dto.response.AuthenticationResponse;
import com.evo.iam.domain.User;
import com.evo.iam.infrastructure.adapter.googleSSO.GoogleAuthService;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserRoleEntityRepository;
import com.evo.iam.application.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleAuthenticationServiceImpl implements GoogleAuthService {
    private final JwtUtil jwtUtil;
    private final UserEntityMapperImpl userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final UserRoleEntityRepository userRoleEntityRepository;

    @Override
    public AuthenticationResponse googleLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        User user = findUser(email, oAuth2User);

        String accessToken = generateToken(() -> jwtUtil.generateAccessToken(user));
        String refreshToken = generateToken(() -> jwtUtil.generateRefreshToken(user));

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserEntity createNewUser(OAuth2User user) {
        String email = user.getAttribute("email");

        UserEntity userEntity = UserEntity.builder()
                .username(user.getAttribute("name"))
                .email(email)
                .firstName(user.getAttribute("given_name"))
                .lastName(user.getAttribute("family_name"))
                .build();
        userEntityRepository.save(userEntity);

//        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
//                .userId(userEntity.getId())
//                .roleName("USER")
//                .build();
//        userRoleEntityRepository.save(userRoleEntity);

        return userEntity;
    }

    private String generateToken(TokenSupplier tokenSupplier) {
        try {
            return tokenSupplier.generate();
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private User findUser(String email, OAuth2User oAuth2User) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(oAuth2User));;

        return userEntityMapper.toDomain(userEntity);
    }

    @FunctionalInterface
    private interface TokenSupplier {
        String generate() throws Exception;
    }
}
