package com.evo.iam.domain.command;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleCmd {
    private UUID id;
    private UUID userId;
    private UUID roleId;
    private Boolean deleted;
}
