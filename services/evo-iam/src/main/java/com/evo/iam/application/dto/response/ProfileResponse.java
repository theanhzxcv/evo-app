package com.evo.iam.application.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String emailAddress;
    private String username;
    private String firstname;
    private String lastname;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
}