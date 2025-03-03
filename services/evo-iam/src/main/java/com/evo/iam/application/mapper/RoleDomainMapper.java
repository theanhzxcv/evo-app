package com.evo.iam.application.mapper;

import com.evo.iam.application.dto.request.RoleCreationRequest;
import com.evo.iam.application.dto.request.RoleUpdateRequest;
import com.evo.iam.application.dto.response.RoleResponse;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.domain.Role;
import com.evo.iam.domain.RolePermission;
import com.evo.iam.domain.command.RoleCreationCmd;
import com.evo.iam.domain.command.RoleUpdateCmd;
import org.springframework.stereotype.Component;

@Component
public class RoleDomainMapper {

    public RoleCreationCmd createFrom(RoleCreationRequest roleCreationRequest) {
        if (roleCreationRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return RoleCreationCmd.builder()
                .name(roleCreationRequest.getName())
                .root("Yes".equalsIgnoreCase(roleCreationRequest.getRoot()))
                .build();
    }

    public void updateFrom(RoleUpdateCmd roleUpdateCmd, RoleUpdateRequest roleUpdateRequest) {
        if (roleUpdateRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        roleUpdateCmd.setRoot("Yes".equalsIgnoreCase(roleUpdateRequest.getRoot()));
    }

    public RoleResponse responseFrom(Role role) {
        if (role == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .root(role.getRoot())
                .deleted(role.getDeleted())
                .permissionIds(role.getRolePermissions() != null ?
                        role.getRolePermissions().stream()
                        .map(RolePermission::getPermissionId)
                        .toList() : null)
                .createdBy(role.getCreatedBy())
                .createdAt(role.getCreatedAt())
                .lastModifiedBy(role.getLastModifiedBy())
                .lastModifiedAt(role.getLastModifiedAt())
                .build();
    }
}
