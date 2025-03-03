package com.evo.storage.application.dto.requests;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateRequest {
    private String description;
    private String owner;
    private String setPublic;
}
