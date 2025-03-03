package com.evo.iam.domain.command;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityCmd {
    private UUID id;
    private String ip;
    private String email;
    private String activity;
    private Instant logAt;
}
