package com.evo.storage.application.dto.requests;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSearchRequest {
    private String keyword;
    private int pageIndex = 1;
    private int pageSize = 10;
}
