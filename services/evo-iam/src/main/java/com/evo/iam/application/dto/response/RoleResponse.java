package com.evo.iam.application.dto.response;

import com.evo.iam.domain.Permission;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private UUID id;
    private String name;
    private boolean root;
    private boolean deleted;
    private List<UUID> permissionIds;

    private String createdBy;
    private Instant createdAt;
    private String lastModifiedBy;
    private Instant lastModifiedAt;
}
