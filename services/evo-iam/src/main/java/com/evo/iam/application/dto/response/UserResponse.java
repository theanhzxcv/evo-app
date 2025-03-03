package com.evo.iam.application.dto.response;

import com.evo.iam.domain.UserRole;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private UUID avatarId;
    private String username;
    private String emailAddress;
    private String password;
    private String firstname;
    private String lastname;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String status;
    private Boolean tfaEnabled;
    private String secretKey;
    private Boolean verified;
    private Boolean deleted;
    private List<UUID> roleIds;

    private String createdBy;
    private Instant createdAt;
    private String lastModifiedBy;
    private Instant lastModifiedAt;
}
