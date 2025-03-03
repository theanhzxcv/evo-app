package com.evo.iam.domain.command;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateCmd {
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private LocalDate dateOfBirth;
    private Boolean tfaEnabled;
    private String secretKey;
    private Boolean verified;
    private List<UserRoleCmd> userRoleCmds;
}
