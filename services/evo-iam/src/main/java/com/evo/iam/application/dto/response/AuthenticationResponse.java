package com.evo.iam.application.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private boolean tfaRequired;
}
