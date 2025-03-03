package com.evo.iam.application.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private String originalName;
    private String description;
    private String type;
    private String extension;
    private String contentType;
    private String owner;
    private String visibility;
    private Boolean deleted;

    private String createdBy;
    private Instant createdAt;
    private String lastModifiedBy;
    private Instant lastModifiedAt;
}
