package com.evo.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
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

    private List<String> roles;
}
