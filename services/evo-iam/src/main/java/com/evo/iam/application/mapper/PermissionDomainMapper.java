package com.evo.iam.application.mapper;

import com.evo.iam.application.dto.request.PermissionCreationRequest;
import com.evo.iam.application.dto.request.PermissionUpdateRequest;
import com.evo.iam.application.dto.response.PermissionResponse;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.domain.Permission;
import com.evo.iam.domain.command.PermissionCreationCmd;
import com.evo.iam.domain.command.PermissionUpdateCmd;
import org.springframework.stereotype.Component;

@Component
public class PermissionDomainMapper {

    public PermissionCreationCmd createFrom(PermissionCreationRequest permissionCreationRequest) {
        if (permissionCreationRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return PermissionCreationCmd.builder()
                .name(permissionCreationRequest.getName())
                .resource(permissionCreationRequest.getResource())
                .scope(permissionCreationRequest.getScope())
                .build();
    }

    public void updateFrom(PermissionUpdateRequest permissionUpdateRequest,
                           PermissionUpdateCmd permissionUpdateCmd) {
        if (permissionUpdateRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        permissionUpdateCmd.setResource(permissionUpdateRequest.getResource());
        permissionUpdateCmd.setScope(permissionUpdateRequest.getScope());
    }


    public PermissionResponse responseFrom(Permission permission) {
        if (permission == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .resource(permission.getResource())
                .scope(permission.getScope())
                .deleted(permission.getDeleted())
                .createdBy(permission.getCreatedBy())
                .createdAt(permission.getCreatedAt())
                .lastModifiedBy(permission.getLastModifiedBy())
                .lastModifiedAt(permission.getLastModifiedAt())
                .build();
    }
}
