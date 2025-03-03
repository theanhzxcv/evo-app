package com.evo.iam.domain.command;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadAvatarCmd {
    private UUID avatarId;
}
