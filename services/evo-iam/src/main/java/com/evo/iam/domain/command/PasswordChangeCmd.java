package com.evo.iam.domain.command;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeCmd {
    private String password;
}
