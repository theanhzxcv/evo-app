package com.evo.iam.domain.command;

import com.evo.iam.domain.Permission;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreationCmd {
    private UUID id;
    private String name;
    private Boolean root;
    private List<RolePermissionCmd> rolePermissionCmds;
}
