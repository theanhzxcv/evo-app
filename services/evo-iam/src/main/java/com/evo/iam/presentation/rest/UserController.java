package com.evo.iam.presentation.rest;

import com.evo.iam.application.dto.request.PasswordChangeRequest;
import com.evo.iam.application.dto.request.PasswordResetRequest;
import com.evo.iam.application.dto.request.ProfileUpdateRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.ProfileResponse;
//import com.evo.iam.application.facatory.ChangePasswordServiceFactory;
import com.evo.iam.application.facatory.PasswordServiceFactory;
import com.evo.iam.application.service.PasswordCmdService;
import com.evo.iam.application.service.impl.command.ProfileCmdServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@Tag(name = "User")
public class UserController {

    private final ProfileCmdServiceImpl userServiceImpl;
    private final PasswordServiceFactory changePasswordFactory;

    @GetMapping
    public ApiResponse<ProfileResponse> myProfile() {
        ProfileResponse myProfile = userServiceImpl.myProfile();

        return ApiResponse.of(myProfile)
                .success(myProfile.getUsername() + "'s profile.");
    }

    @PutMapping
    public ApiResponse<ProfileResponse> updateProfile(
            @ParameterObject @Valid ProfileUpdateRequest profileUpdateRequest) {
        ProfileResponse profileUpdated = userServiceImpl.updateProfile(profileUpdateRequest);

        return ApiResponse.of(profileUpdated)
                .success(profileUpdated.getUsername() + "'s profile updated");
    }

    @PostMapping(path = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadProfileImage(@RequestParam("image") MultipartFile image) {

        return userServiceImpl.uploadProfileImage(image);
    }

    @PutMapping("/password")
    public ApiResponse<String> changePassword(
            @ParameterObject @Valid PasswordChangeRequest passwordChangeRequest) {
        PasswordCmdService changePasswordService = changePasswordFactory.getChangePasswordService();
        String passwordChanged = changePasswordService.changePassword(passwordChangeRequest);

        return ApiResponse.of(passwordChanged)
                .success("Your password changed");
    }

    @PostMapping("/password/forgot")
    public ApiResponse<String> forgotPassword(@RequestParam String email) {
        String forgotPassword = userServiceImpl.forgotPassword(email);

        return ApiResponse.of(forgotPassword)
                .success("Password reset email sent.");
    }

    @PutMapping("/password/reset")
    public ApiResponse<String> resetPassword(
            @ParameterObject @Valid PasswordResetRequest passwordResetRequest,
            HttpServletRequest request) {
        String resetPassword = userServiceImpl.resetPassword(passwordResetRequest, request);

        return ApiResponse.of(resetPassword)
                .success("Your password reset");
    }
}
