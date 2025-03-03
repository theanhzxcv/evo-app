package com.evo.iam.domain;

import com.evo.config.AuditableDomain;
import com.evo.iam.domain.command.RoleCreationCmd;
import com.evo.iam.domain.command.RolePermissionCmd;
import com.evo.iam.domain.command.RoleUpdateCmd;
import com.evo.util.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuperBuilder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuditableDomain {
    private UUID id;
    private String name;
    private Boolean root;
    private Boolean deleted;
    private List<RolePermission> rolePermissions;

    public Role(RoleCreationCmd cmd) {
        this.id = IdUtils.nextId();
        this.name = cmd.getName();
        this.root = cmd.getRoot();
        this.deleted = false;
        this.updateRolePermission(cmd.getRolePermissionCmds());
    }

    public void update(RoleUpdateCmd cmd) {
        this.root = cmd.getRoot();
        this.updateRolePermission(cmd.getRolePermissionCmds());
    }

    public void updateRolePermission(List<RolePermissionCmd> cmds) {
        if (CollectionUtils.isEmpty(this.rolePermissions)) {
            this.rolePermissions = new ArrayList<>();
        }

        Map<UUID, RolePermission> existingPermissionMap = this.rolePermissions.stream()
                .collect(Collectors.toMap(RolePermission::getPermissionId, rolePermission -> {
                    rolePermission.delete();
                    return rolePermission;
                }));

        cmds.stream()
                .filter(cmd -> existingPermissionMap.containsKey(cmd.getPermissionId()))
                .forEach(cmd -> existingPermissionMap.get(cmd.getPermissionId()).undelete());

        cmds.stream()
                .filter(cmd -> !existingPermissionMap.containsKey(cmd.getPermissionId()))
                .map(cmd -> RolePermission.builder()
                        .id(IdUtils.nextId())
                        .roleId(this.id)
                        .permissionId(cmd.getPermissionId())
                        .deleted(false)
                        .build())
                .forEach(this.rolePermissions::add);
    }


    public void delete() {
        this.deleted = true;
        if (!CollectionUtils.isEmpty(this.rolePermissions)) {
            this.rolePermissions.forEach(RolePermission::delete);
        }
    }

    public void restore() {
        this.deleted = false;
        if (!CollectionUtils.isEmpty(this.rolePermissions)) {
            this.rolePermissions.forEach(RolePermission::undelete);
        }
    }

    public void enrichRolePermission(List<RolePermission> rolePermissions) {
        if (CollectionUtils.isEmpty(this.rolePermissions)) {
            this.rolePermissions = new ArrayList<>();
        }

        this.rolePermissions.addAll(rolePermissions);
    }
}
