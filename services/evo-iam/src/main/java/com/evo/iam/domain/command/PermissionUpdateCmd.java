package com.evo.iam.domain.command;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUpdateCmd {
    private String resource;
    private String scope;
}
