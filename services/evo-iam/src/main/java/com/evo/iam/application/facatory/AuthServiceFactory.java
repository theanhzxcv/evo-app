package com.evo.iam.application.facatory;

import com.evo.iam.application.service.AuthenticationCmdService;
import com.evo.iam.application.service.impl.command.ApplicationAuthenticationServiceImpl;
import com.evo.iam.application.service.impl.command.KeycloakAuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceFactory {
    @Value("${keycloak.enabled}")
    private boolean isKeycloakEnabled;

    private final KeycloakAuthenticationServiceImpl keycloakAuthService;
    private final ApplicationAuthenticationServiceImpl applicationAuthService;

    public AuthServiceFactory(KeycloakAuthenticationServiceImpl keycloakAuthService,
                              ApplicationAuthenticationServiceImpl applicationAuthService) {
        this.keycloakAuthService = keycloakAuthService;
        this.applicationAuthService = applicationAuthService;
    }

    public AuthenticationCmdService getAuthService() {
        if (isKeycloakEnabled) {
            return keycloakAuthService;
        } else {
            return applicationAuthService;
        }
    }
}
