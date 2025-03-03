package com.evo.iam.infrastructure.persistence.entities;

import com.evo.config.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class PermissionEntity extends AuditableEntity {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "resource")
    private String resource;

    @Column(name = "scope")
    private String scope;

    @Column(name = "deleted")
    private Boolean deleted;
}
