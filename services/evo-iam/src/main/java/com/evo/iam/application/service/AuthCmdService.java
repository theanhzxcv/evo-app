package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.SignInRequest;
import com.evo.iam.application.dto.request.SignOutRequest;
import com.evo.iam.application.dto.request.SignUpRequest;
import com.evo.iam.application.dto.request.VerificationRequest;
import com.evo.iam.application.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthCmdService {
    AuthenticationResponse login(SignInRequest signInRequest, HttpServletRequest request);

    String registration(SignUpRequest signUpRequest, HttpServletRequest request);

    AuthenticationResponse verification(VerificationRequest verificationRequest);

    AuthenticationResponse refreshToken(String refreshToken);

    String logout(SignOutRequest signOutRequest, HttpServletRequest request);
}
