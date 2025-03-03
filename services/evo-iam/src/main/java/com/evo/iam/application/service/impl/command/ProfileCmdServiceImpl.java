package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.PasswordResetRequest;
import com.evo.iam.application.dto.request.ProfileUpdateRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.ProfileResponse;
import com.evo.iam.application.mapper.UserActivityDomainMapper;
import com.evo.iam.domain.User;
import com.evo.iam.domain.command.PasswordChangeCmd;
import com.evo.iam.domain.command.UploadAvatarCmd;
import com.evo.iam.domain.command.UserUpdateCmd;
import com.evo.iam.domain.repository.UserDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.infrastructure.adapter.feign.PrivateFileClient;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.iam.application.security.JwtUtil;
import com.evo.iam.application.service.ProfileCmdService;
import com.evo.iam.infrastructure.adapter.emailSender.impl.EmailServiceImpl;
import com.evo.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileCmdServiceImpl implements ProfileCmdService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailServiceImpl;
    private final PrivateFileClient privateFileClient;

    private final UserEntityMapperImpl userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final UserDomainMapper userDomainMapper;
    private final UserDomainRepository userDomainRepository;
    private final UserActivityDomainMapper userActivityDomainMapper;

    private final static long REFRESH_PASSWORD_TOKEN_EXPIRATION = 1000 * 60 * 5;

    @Override
    public ProfileResponse myProfile() {
        String email = getCurrentUserEmail();
        User user = findUser(email);

        return userDomainMapper.profileResponseFrom(user);
    }

    @Override
    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        String email = getCurrentUserEmail();
        User user = findUser(email);
        UserUpdateCmd cmd = new UserUpdateCmd();
        userDomainMapper.profileUpdateFrom(cmd, request);
        user.updateProfile(cmd);
        userDomainRepository.save(user);

        return userDomainMapper.profileResponseFrom(user);
    }

    @Override
    public ApiResponse<String> uploadProfileImage(MultipartFile image) {
        UUID avatarId = UUID.fromString(privateFileClient.uploadProfileImage(image).getData());
        String email = getCurrentUserEmail();
        User user = findUser(email);
        UploadAvatarCmd cmd = UploadAvatarCmd.builder()
                .avatarId(avatarId)
                .build();
        user.uploadAvatar(cmd);
        userDomainRepository.save(user);

        return ApiResponse.ok();
    }

    @Override
    public String forgotPassword(String email) {
        User user = findUser(email);
        try {
            String resetPasswordToken = jwtUtil.generateToken(user, REFRESH_PASSWORD_TOKEN_EXPIRATION);
            emailServiceImpl.sendResetPasswordEmail(email, resetPasswordToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return "Reset instructions have been sent to your email address. " +
                "Check your inbox or spam folder.";
    }

    @Override
    public String resetPassword(PasswordResetRequest request, HttpServletRequest httpRequest) {
        final String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        String resetPasswordToken = authHeader.substring(7);
        String email = jwtUtil.extractEmail(resetPasswordToken);

        User user = findUser(email);
        PasswordChangeCmd cmd = new PasswordChangeCmd();
        userDomainMapper.resetPasswordFrom(cmd, request);
        cmd.setPassword(passwordEncoder.encode(request.getResetPassword()));
        user.changePassword(cmd);

        userDomainRepository.save(user);
        return "Your password has been reset. " +
                "You can now log in with your new password";
    }

    private String getCurrentUserEmail() {
        return SecurityUtils.getCurrentUser().orElse("anonymous");
    }

    private User findUser(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userEntityMapper.toDomain(userEntity);
    }
}
