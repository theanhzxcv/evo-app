package com.evo.iam.domain.command;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationCmd {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean tfaEnabled;
    private String secretKey;
    private Boolean verified;
    private UserRoleCmd userRoleCmd;
}
