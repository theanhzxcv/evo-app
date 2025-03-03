package com.evo.iam.domain.command;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionCmd {
    private UUID id;
    private UUID roleId;
    private UUID permissionId;
    private Boolean deleted;
}
