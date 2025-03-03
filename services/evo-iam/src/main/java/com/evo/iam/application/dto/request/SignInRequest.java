package com.evo.iam.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotBlank(message = "FIELD_MISSING")
    @Email(message = "LOGIN_FAILED")
    private String email;

    @NotBlank(message = "FIELD_MISSING")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=_]).{8,}$", message = "PASSWORD_POLICY_VIOLATION")
    private String password;
}
