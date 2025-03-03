package com.evo.iam.domain;

import com.evo.config.AuditableDomain;
import com.evo.iam.domain.command.UserRoleCmd;
import com.evo.util.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends AuditableDomain {
    private UUID id;
    private UUID userId;
    private UUID roleId;
    private Boolean deleted;

    public UserRole(UserRoleCmd cmd) {
        this.id = cmd.getId();
        this.userId = cmd.getUserId();
        this.roleId = cmd.getRoleId();
        this.deleted = cmd.getDeleted();
    }

    public void delete() {
        this.deleted = true;
    }

    public void undelete() {
        this.deleted = false;
    }
}
