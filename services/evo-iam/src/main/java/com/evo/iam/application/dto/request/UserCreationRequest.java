package com.evo.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @NotBlank(message = "FIELD_MISSING")
    private String username;

    @NotBlank(message = "FIELD_MISSING")
    private String email;

    @NotBlank(message = "FIELD_MISSING")
    private String password;

    @NotBlank(message = "FIELD_MISSING")
    private String firstname;

    @NotBlank(message = "FIELD_MISSING")
    private String lastname;

    @NotBlank(message = "FIELD_MISSING")
    private String enableTfa;

    private List<String> roles;
}
