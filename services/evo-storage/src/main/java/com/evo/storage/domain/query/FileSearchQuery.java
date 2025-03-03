package com.evo.storage.domain.query;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSearchQuery {
    private String keyword;
    private int pageIndex;
    private int pageSize;
}
