package com.evo.security;

import com.evo.config.ActionLogFilter;
import com.evo.config.CustomPermissionEvaluator;
import com.evo.config.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@EnableWebSecurity
@EnableFeignClients(basePackages = {"com.evo.client"})
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class HttpSecurityConfiguration {

//    private final ActionLogFilter actionLogFilter;
    private final ForbiddenTokenFilter forbiddenTokenFilter;
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final CustomPermissionEvaluator customPermissionEvaluator;
    private final JwtProperties jwtProperties;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement
                        -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/",
                                        "/health",
                                        "/api/public/**",
                                        "/api/auth/sign-in",
                                        "/api/auth/activate/{email}",
                                        "/api/auth/sign-up",
                                        "/api/auth/verify",
                                        "/api/auth/token/refresh",
                                        "/api/profile/password/forgot",
                                        "/swagger-ui/**", "/v3/api-docs/**",
                                        "/api/certificate/.well-known/jwks.json").permitAll()
                                .anyRequest().authenticated()
                )
//                .oauth2Login(oauth2
//                        -> oauth2.defaultSuccessUrl("/api/auth/home", true))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(this.jwkResolver(this.jwtProperties)));
        http.addFilterAfter(this.forbiddenTokenFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(this.customAuthenticationFilter, BearerTokenAuthenticationFilter.class);
//        http.addFilterAfter(this.actionLogFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        var expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(customPermissionEvaluator);
        return expressionHandler;
    }

    public AuthenticationManagerResolver<HttpServletRequest> jwkResolver(JwtProperties jwtProperties) {
        return new JwkAuthManagerResolver(jwtProperties);
    }
}
