package com.evo.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {
    @NotBlank(message = "FIELD_MISSING")
    private String email;

    @NotBlank(message = "FIELD_MISSING")
    private int otp;
}
