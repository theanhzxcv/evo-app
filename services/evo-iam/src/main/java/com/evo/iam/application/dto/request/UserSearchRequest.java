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
public class UserSearchRequest{
    @NotBlank(message = "FIELD_MISSING")
    private String keyword;

    private int pageIndex = 1;
    private int pageSize = 10;
}
