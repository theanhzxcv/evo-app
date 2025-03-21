package com.evo.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUpdateRequest {
    @NotBlank(message = "FIELD_MISSING")
    private String resource;

    @NotBlank(message = "FIELD_MISSING")
    private String scope;
}
