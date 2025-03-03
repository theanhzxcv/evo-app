package com.evo.iam.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_activity")
public class UserActivityEntity {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "ip_address")
    private String ip;

    @Column(name = "email")
    private String email;

    @Column(name = "activity")
    private String activity;

    @Column(name = "log_at")
    private Instant logAt;
}
