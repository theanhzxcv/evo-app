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
public class RoleUpdateRequest {
    @NotBlank(message = "FIELD_MISSING")
    private String root;

    private List<String> permissions;
}
