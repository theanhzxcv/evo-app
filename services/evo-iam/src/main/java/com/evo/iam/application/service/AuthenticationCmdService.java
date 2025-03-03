package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.SignInRequest;
import com.evo.iam.application.dto.request.SignOutRequest;
import com.evo.iam.application.dto.request.SignUpRequest;
import com.evo.iam.application.dto.request.VerificationRequest;
import com.evo.iam.application.dto.request.ActivateAccountRequest;
import com.evo.iam.application.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationCmdService {

    AuthenticationResponse signIn(SignInRequest request, HttpServletRequest httpRequest);

    String signUp(SignUpRequest request, HttpServletRequest httpRequest);

    String activateAccount(String email, ActivateAccountRequest request, HttpServletRequest httpRequest);

    AuthenticationResponse verification(VerificationRequest request, HttpServletRequest httpRequest);

    AuthenticationResponse refreshToken(String refreshToken);

    String logout(SignOutRequest request, HttpServletRequest httpRequest);
}
