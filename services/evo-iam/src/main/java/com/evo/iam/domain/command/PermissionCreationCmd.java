package com.evo.iam.domain.command;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCreationCmd {
    private UUID id;
    private String name;
    private String resource;
    private String scope;
}
