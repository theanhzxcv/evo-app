package com.evo.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivateAccountRequest {

    @NotBlank(message = "FIELD_MISSING")
    private String address;

    @NotBlank(message = "FIELD_MISSING")
//    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "INVALID_PHONE_NUMBER")
    private String phone;

    @Past(message = "INVALID_DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
}
