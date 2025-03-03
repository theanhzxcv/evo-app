package com.evo.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("System");
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt token = jwtAuth.getToken();
            String email = token.getClaimAsString("email");
            if (!StringUtils.hasText(email)) {
                email = token.getClaimAsString("sub");
            }
            return Optional.ofNullable(email).filter(StringUtils::hasText).or(() -> Optional.of("System"));
        }

        return Optional.of(authentication.getName());
    }
}
