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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity extends AuditableEntity {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "avatar_id")
    private UUID avatarId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "account_status")
    private String status;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "tfa_enable", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean tfaEnabled;

    @Column(name = "verified", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean verified;

    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted;
}
