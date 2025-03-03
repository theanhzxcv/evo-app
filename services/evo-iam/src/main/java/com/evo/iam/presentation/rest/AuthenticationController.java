package com.evo.iam.presentation.rest;

import com.evo.iam.application.dto.request.SignInRequest;
import com.evo.iam.application.dto.request.SignOutRequest;
import com.evo.iam.application.dto.request.SignUpRequest;
import com.evo.iam.application.dto.request.VerificationRequest;
import com.evo.iam.application.dto.request.ActivateAccountRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.AuthenticationResponse;
import com.evo.iam.application.facatory.AuthServiceFactory;
import com.evo.iam.application.service.AuthenticationCmdService;
import com.evo.iam.infrastructure.adapter.googleSSO.impl.GoogleAuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthenticationController {

    private final GoogleAuthenticationServiceImpl googleAuthService;
    private final AuthServiceFactory authServiceFactory;

    @GetMapping("/home")
    public ApiResponse<AuthenticationResponse> googleLogin(@AuthenticationPrincipal OAuth2User user) {
        AuthenticationResponse response = googleAuthService.googleLogin(user);

        return ApiResponse.of(response)
                .success("Welcome back, " + user.getAttribute("given_name") + "!");
    }

    @PostMapping("/sign-in")
    public ApiResponse<AuthenticationResponse> signIn(
            @ParameterObject @Valid SignInRequest signInRequest,
            HttpServletRequest httpRequest) {
        AuthenticationCmdService authService = authServiceFactory.getAuthService();
        AuthenticationResponse response = authService.signIn(signInRequest, httpRequest);

        String message = response.isTfaRequired()
                ? "Verification required. Please complete two-factor authentication."
                : "Signed in successfully. Welcome back!";

        return ApiResponse.of(response).success(message);
    }

    @PutMapping("/activate/{email}")
    public ApiResponse<String> activateAccount(
            @PathVariable("email") String email,
            @ParameterObject @Valid ActivateAccountRequest request,
            HttpServletRequest httpRequest) {
        AuthenticationCmdService authService = authServiceFactory.getAuthService();
        String activatedAccount = authService.activateAccount(email, request, httpRequest);

        return ApiResponse.of(activatedAccount).success("Your account has been activated successfully.");
    }

    @PostMapping("/verify")
    public ApiResponse<AuthenticationResponse> verify(
            @ParameterObject @Valid VerificationRequest verificationRequest,
            HttpServletRequest httpRequest) {
        AuthenticationCmdService authService = authServiceFactory.getAuthService();
        AuthenticationResponse response = authService.verification(verificationRequest, httpRequest);

        return ApiResponse.of(response).success("Your account has been verified successfully.");
    }

    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(
            @ParameterObject @Valid SignUpRequest signUpRequest,
            HttpServletRequest httpRequest) {
        AuthenticationCmdService authService = authServiceFactory.getAuthService();
        String response = authService.signUp(signUpRequest, httpRequest);

        return ApiResponse.of(response).success("Sign-up successful! Welcome to IAM.");
    }

    @PostMapping("/token/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestParam String refreshToken)
            throws IOException {
        AuthenticationCmdService authService = authServiceFactory.getAuthService();
        AuthenticationResponse response = authService.refreshToken(refreshToken);

        return ApiResponse.of(response).success("Access token refreshed successfully.");
    }

    @DeleteMapping("/logout")
    public ApiResponse<String> logout(
            @ParameterObject @Valid SignOutRequest signOutRequest,
            HttpServletRequest request) {
        AuthenticationCmdService authService = authServiceFactory.getAuthService();
        String response = authService.logout(signOutRequest, request);

        return ApiResponse.of(response).success("You have been logged out successfully.");
    }
}

