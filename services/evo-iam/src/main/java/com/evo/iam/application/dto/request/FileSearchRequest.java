package com.evo.iam.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileSearchRequest {
    private String keyword;
    private int pageIndex = 1;
    private int pageSize = 10;
}
