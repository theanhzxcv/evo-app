package com.evo.iam.infrastructure.persistence.entities;

import com.evo.config.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class RoleEntity extends AuditableEntity {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "root")
    private Boolean root;

    @Column(name = "deleted")
    private Boolean deleted;
}
