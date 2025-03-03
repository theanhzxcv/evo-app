package com.evo.iam.domain.command;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateCmd {
    private Boolean root;
    private List<RolePermissionCmd> rolePermissionCmds;
}
