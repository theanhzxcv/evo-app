package com.evo.iam.presentation.rest;

import com.evo.iam.application.dto.request.UserCreationRequest;
import com.evo.iam.application.dto.request.UserUpdateRequest;
import com.evo.iam.application.dto.request.PasswordChangeRequest;
import com.evo.iam.application.dto.request.UserSearchRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.UserResponse;
import com.evo.iam.application.dto.response.api.PageApiResponse;
import com.evo.iam.application.service.UserCmdService;
import com.evo.iam.application.service.UserQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Management")
public class ManagementController {

    private final UserCmdService userCmdService;
    private final UserQueryService userQueryService;

    @PreAuthorize("hasPermission('User','Create')")
    @PostMapping
    public ApiResponse<UserResponse> createUser(
            @ParameterObject @Valid UserCreationRequest userCreationRequest) {
        try {
            UserResponse newUser = userCmdService.create(userCreationRequest);

            return ApiResponse.created(newUser).success("New user created");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to create new user.");
        }

    }

    @PreAuthorize("hasPermission('User','Read')")
    @GetMapping
    public PageApiResponse<UserResponse> allUsers(@RequestParam int pageIndex,
                                                  @RequestParam int pageSize) {

        try {
            Page<UserResponse> allUsers = userQueryService.allUsers(pageIndex, pageSize);

            return PageApiResponse.of(allUsers.getContent(),
                    allUsers.getNumber(),
                    allUsers.getSize(),
                    allUsers.getTotalElements());
        } catch (Exception e) {
            return PageApiResponse.failPaging("Failed to retrieve users.");
        }
    }

    @PreAuthorize("hasPermission('User','Read')")
    @GetMapping("/search")
    public PageApiResponse<UserResponse> findUserByKeyword(
            @ParameterObject @Valid UserSearchRequest userSearchRequest
    ) {
        try {
            Page<UserResponse> userMatchedFound = userQueryService.searchUser(userSearchRequest);

            return PageApiResponse.of(userMatchedFound.getContent(),
                    userMatchedFound.getNumber(),
                    userMatchedFound.getSize(),
                    userMatchedFound.getTotalElements());
        } catch (Exception e) {
            return PageApiResponse.failPaging("Failed to retrieve users with keyword: "
                    + userSearchRequest.getKeyword());
        }
    }

    @PreAuthorize("hasPermission('User','Update')")
    @PutMapping("/{email}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("email") String email,
            @ParameterObject @Valid UserUpdateRequest userUpdateRequest) {
        try {
            UserResponse updatedUser = userCmdService.update(email, userUpdateRequest);

            return ApiResponse.of(updatedUser)
                    .success("User with email: " + userUpdateRequest.getUsername() + " updated");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to update user.");
        }
    }

    @PutMapping("/keycloak/password")
    public ApiResponse<String> changeKeycloakPassword(
            @RequestParam String email,
            @ParameterObject @Valid PasswordChangeRequest request) {
        try {
            String changedPassword = userCmdService.changeKeyCloakPassword(email, request);

            return ApiResponse.of(changedPassword).success("Password changed");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to change password.");
        }
    }

    @PutMapping("/system/change-password/")
    public ApiResponse<String> changeSystemPassword(
            @RequestParam String email,
            @ParameterObject @Valid PasswordChangeRequest request) {
        String changedPassword = userCmdService.changeSystemPassword(email, request);

        return ApiResponse.of(changedPassword).success("Password changed");
    }

    @PreAuthorize("hasPermission('User','Delete')")
    @DeleteMapping("/{email}")
    public ApiResponse<UserResponse> deleteUser(@PathVariable("email") String emailAddress) {
        UserResponse deletedUser = userCmdService.delete(emailAddress);

        return ApiResponse.of(deletedUser).success("User deleted");
    }

    @PreAuthorize("hasPermission('User','Update')")
    @PutMapping("/undelete/{email}")
    public ApiResponse<UserResponse> undeleteUser(@PathVariable("email") String emailAddress) {
        UserResponse undeletedUser = userCmdService.restore(emailAddress);

        return ApiResponse.of(undeletedUser).success("User undeleted");
    }
}
