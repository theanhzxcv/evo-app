package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.PasswordResetRequest;
import com.evo.iam.application.dto.request.ProfileUpdateRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.ProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileCmdService {

    ProfileResponse myProfile();

    ProfileResponse updateProfile(ProfileUpdateRequest request);

    ApiResponse<String> uploadProfileImage(MultipartFile image);

    String forgotPassword(String email);

    String resetPassword(PasswordResetRequest request, HttpServletRequest httpRequest);
}
