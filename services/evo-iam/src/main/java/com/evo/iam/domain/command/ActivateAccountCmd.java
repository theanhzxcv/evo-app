package com.evo.iam.domain.command;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivateAccountCmd {
    private String address;
    private String phone;
    private LocalDate dateOfBirth;
}
