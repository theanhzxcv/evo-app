package com.evo.iam.application.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    private UUID id;
    private String name;
    private String resource;
    private String scope;
    private boolean deleted;

    private String createdBy;
    private Instant createdAt;
    private String lastModifiedBy;
    private Instant lastModifiedAt;
}
