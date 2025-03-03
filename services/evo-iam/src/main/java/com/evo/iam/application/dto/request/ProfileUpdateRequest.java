package com.evo.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    @NotBlank(message = "FIELD_MISSING")
    private String username;

    @NotBlank(message = "FIELD_MISSING")
    private String firstname;

    @NotBlank(message = "FIELD_MISSING")
    private String lastname;

    @NotBlank(message = "FIELD_MISSING")
    private String address;

    @NotBlank(message = "FIELD_MISSING")
    private String phoneNumber;

    @NotNull(message = "FIELD_MISSING")
    private LocalDate dateOfBirth;

    @NotBlank(message = "FIELD_MISSING")
    private String enableTfa;
}
