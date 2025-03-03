package com.evo.iam.application.mapper;

import com.evo.iam.domain.Permission;
import com.evo.iam.domain.command.RolePermissionCmd;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RolePermissionDomainMapper {
    public RolePermissionCmd toRolePermissionCmd(Permission permission) {
        return RolePermissionCmd.builder()
//                .roleId(roleId)
                .permissionId(permission.getId())
                .build();
    }

    public List<RolePermissionCmd> toRolePermissionCmdList(List<Permission> permissions) {
        return permissions.stream()
                .map(this::toRolePermissionCmd)
                .collect(Collectors.toList());
    }
}
