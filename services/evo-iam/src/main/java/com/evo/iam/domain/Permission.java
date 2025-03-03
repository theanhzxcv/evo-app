package com.evo.iam.domain;

import com.evo.config.AuditableDomain;
import com.evo.iam.domain.command.PermissionCreationCmd;
import com.evo.iam.domain.command.PermissionUpdateCmd;
import com.evo.util.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends AuditableDomain {
    private UUID id;
    private String name;
    private String resource;
    private String scope;
    private Boolean deleted;

    public Permission(PermissionCreationCmd cmd) {
        this.id = IdUtils.nextId();
        this.name = cmd.getName();
        this.resource = cmd.getResource();
        this.scope = cmd.getScope();
        this.deleted = false;
    }

    public void update(PermissionUpdateCmd cmd) {
        this.resource = cmd.getResource();
        this.scope = cmd.getScope();
    }

    public void delete() {
        this.deleted = true;
    }

    public void undelete() {
        this.deleted = false;
    }
}
