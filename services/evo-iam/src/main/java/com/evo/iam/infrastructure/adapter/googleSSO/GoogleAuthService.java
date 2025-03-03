package com.evo.iam.infrastructure.adapter.googleSSO;

import com.evo.iam.application.dto.response.AuthenticationResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface GoogleAuthService {

    AuthenticationResponse googleLogin(OAuth2User user);
}
