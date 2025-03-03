package com.evo.iam.domain.query;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchQuery {
    private String keyword;
    private int pageIndex;
    private int pageSize;
}
