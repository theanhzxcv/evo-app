package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.PasswordChangeRequest;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.application.service.PasswordCmdService;
import com.evo.iam.domain.User;
import com.evo.iam.domain.command.PasswordChangeCmd;
import com.evo.iam.domain.repository.UserDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemPasswordCmdServiceImpl implements PasswordCmdService {

    private final PasswordEncoder passwordEncoder;
    private final UserDomainMapper userDomainMapper;
    private final UserDomainRepository userDomainRepository;
    private final UserEntityMapperImpl userEntityMapper;
    private final UserEntityRepository userEntityRepository;

    @Override
    public String changePassword(PasswordChangeRequest request) {
        String email = getCurrentUserEmail();
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User user = userEntityMapper.toDomain(userEntity);

        if (!request.getConfirmPassword().equals(request.getNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        PasswordChangeCmd cmd = new PasswordChangeCmd();
        userDomainMapper.changePasswordFrom(cmd, request);
        cmd.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.changePassword(cmd);

        userDomainRepository.save(user);

        return "Password changed successfully.";
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
