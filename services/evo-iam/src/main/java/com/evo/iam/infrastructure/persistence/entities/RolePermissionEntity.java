package com.evo.iam.infrastructure.persistence.entities;

import com.evo.config.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_permission")
public class RolePermissionEntity extends AuditableEntity {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "permission_id")
    private UUID permissionId;

    @Column(name = "deleted")
    private Boolean deleted;
}
