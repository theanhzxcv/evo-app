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
@Table(name = "user_role")
public class UserRoleEntity extends AuditableEntity {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "deleted")
    private Boolean deleted;
}
