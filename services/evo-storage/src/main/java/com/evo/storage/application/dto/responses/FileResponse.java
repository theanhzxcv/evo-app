package com.evo.storage.application.dto.responses;

import lombok.*;

import java.time.Instant;

@Data
@Builder
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
