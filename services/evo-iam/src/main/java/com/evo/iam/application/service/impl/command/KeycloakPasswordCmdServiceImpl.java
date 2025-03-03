package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.PasswordChangeRequest;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.application.service.PasswordCmdService;
import com.evo.iam.domain.command.PasswordChangeCmd;
import com.evo.iam.infrastructure.adapter.keycloak.KeycloakUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakPasswordCmdServiceImpl implements PasswordCmdService {

    private final KeycloakUtils keycloakUtils;
    private final UserDomainMapper userDomainMapper;

    @Override
    public String changePassword(PasswordChangeRequest request) {

        if (!request.getConfirmPassword().equals(request.getNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        PasswordChangeCmd cmd = new PasswordChangeCmd();
        userDomainMapper.changePasswordFrom(cmd, request);
        keycloakUtils.changeKeycloakPassword(cmd);

        return "Password changed successfully.";
    }
}
