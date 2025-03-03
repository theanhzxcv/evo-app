package com.evo.iam.domain;

import com.evo.config.AuditableDomain;
import com.evo.iam.domain.command.RolePermissionCmd;
import com.evo.util.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission extends AuditableDomain {
    private UUID id;
    private UUID roleId;
    private UUID permissionId;
    private Boolean deleted;

    public RolePermission(RolePermissionCmd cmd) {
        this.id = cmd.getId();
        this.roleId = cmd.getRoleId();
        this.permissionId = cmd.getPermissionId();
        this.deleted = cmd.getDeleted();
    }

    public void delete() {
        this.deleted = true;
    }

    public void undelete() {
        this.deleted = false;
    }
}
