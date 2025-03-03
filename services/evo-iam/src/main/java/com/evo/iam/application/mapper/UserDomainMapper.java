package com.evo.iam.application.mapper;

import com.evo.iam.application.dto.request.*;
import com.evo.iam.application.dto.response.SearchResponse;
import com.evo.iam.application.dto.response.UserResponse;
import com.evo.iam.application.dto.response.ProfileResponse;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.domain.User;
import com.evo.iam.domain.UserRole;
import com.evo.iam.domain.command.*;
import com.evo.iam.domain.query.UserSearchQuery;
import com.evo.util.IdUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDomainMapper {

    public void profileUpdateFrom(UserUpdateCmd userUpdateCmd, ProfileUpdateRequest profileUpdateRequest) {
        if (profileUpdateRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        userUpdateCmd.setUsername(profileUpdateRequest.getUsername());
        userUpdateCmd.setFirstName(profileUpdateRequest.getFirstname());
        userUpdateCmd.setLastName(profileUpdateRequest.getLastname());
        userUpdateCmd.setAddress(profileUpdateRequest.getAddress());
        userUpdateCmd.setPhone(profileUpdateRequest.getPhoneNumber());
        userUpdateCmd.setDateOfBirth(profileUpdateRequest.getDateOfBirth());
        userUpdateCmd.setTfaEnabled("Yes".equalsIgnoreCase(profileUpdateRequest.getEnableTfa()));
        userUpdateCmd.setVerified(!"Yes".equalsIgnoreCase(profileUpdateRequest.getEnableTfa()));
    }

    public ProfileResponse profileResponseFrom(User user) {
        if (user == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return ProfileResponse.builder()
                .emailAddress(user.getEmail())
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .address(user.getAddress())
                .phoneNumber(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    public void changePasswordFrom(PasswordChangeCmd passwordChangeCmd, PasswordChangeRequest passwordChangeRequest) {
        if (passwordChangeRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        passwordChangeCmd.setPassword(passwordChangeRequest.getNewPassword());
    }

    public void resetPasswordFrom(PasswordChangeCmd passwordChangeCmd, PasswordResetRequest passwordResetRequest) {
        if (passwordResetRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        passwordChangeCmd.setPassword(passwordResetRequest.getResetPassword());
    }

    public UserRegistrationCmd signUpFrom(SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return UserRegistrationCmd.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .firstName(signUpRequest.getFirstname())
                .lastName(signUpRequest.getLastname())
                .tfaEnabled("Yes".equalsIgnoreCase(signUpRequest.getEnableTfa()))
                .verified(!"Yes".equalsIgnoreCase(signUpRequest.getEnableTfa()))
                .build();
    }

    public void activateFrom(ActivateAccountCmd activateAccountCmd, ActivateAccountRequest activateAccountRequest) {
        if (activateAccountRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        activateAccountCmd.setAddress(activateAccountRequest.getAddress());
        activateAccountCmd.setPhone(activateAccountRequest.getPhone());
        activateAccountCmd.setDateOfBirth(activateAccountRequest.getDateOfBirth());
    }

    public UserCreationCmd createFrom(UserCreationRequest userCreationRequest) {
        if (userCreationRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return UserCreationCmd.builder()
                .username(userCreationRequest.getUsername())
                .email(userCreationRequest.getEmail())
                .password(userCreationRequest.getPassword())
                .firstName(userCreationRequest.getFirstname())
                .lastName(userCreationRequest.getLastname())
                .tfaEnabled("Yes".equalsIgnoreCase(userCreationRequest.getEnableTfa()))
                .verified(!"Yes".equalsIgnoreCase(userCreationRequest.getEnableTfa()))
                .build();
    }

    public void updateFrom(UserUpdateCmd userUpdateCmd, UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        userUpdateCmd.setUsername(userUpdateRequest.getUsername());
        userUpdateCmd.setFirstName(userUpdateRequest.getFirstname());
        userUpdateCmd.setLastName(userUpdateRequest.getLastname());
        userUpdateCmd.setAddress(userUpdateRequest.getAddress());
        userUpdateCmd.setPhone(userUpdateRequest.getPhoneNumber());
        userUpdateCmd.setDateOfBirth(userUpdateRequest.getDateOfBirth());
        userUpdateCmd.setTfaEnabled("Yes".equalsIgnoreCase(userUpdateRequest.getEnableTfa()));
        userUpdateCmd.setVerified(!"Yes".equalsIgnoreCase(userUpdateRequest.getEnableTfa()));
    }

    public UserResponse responseFrom(User user) {
        if (user == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return UserResponse.builder()
                .id(user.getId())
                .avatarId(user.getAvatarId())
                .username(user.getUsername())
                .emailAddress(user.getEmail())
                .password(user.getPassword())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .address(user.getAddress())
                .phoneNumber(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .deleted(user.getDeleted())
                .status(user.getStatus())
                .tfaEnabled(user.getTfaEnabled())
                .secretKey(user.getSecretKey())
                .verified(user.getVerified())
                .roleIds(user.getUserRoles() != null ?
                        user.getUserRoles().stream()
                                .map(UserRole::getRoleId)
                                .toList() :  null)
                .createdBy(user.getCreatedBy())
                .createdAt(user.getCreatedAt())
                .lastModifiedBy(user.getLastModifiedBy())
                .lastModifiedAt(user.getLastModifiedAt())
                .build();
    }

    public UserSearchQuery searchFrom(UserSearchRequest userSearchRequest) {
        if (userSearchRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return UserSearchQuery.builder()
                .keyword(userSearchRequest.getKeyword())
                .pageIndex(userSearchRequest.getPageIndex())
                .pageSize(userSearchRequest.getPageSize())
                .build();
    }

    public UserCreationCmd createFrom(String email, String password) {
        return UserCreationCmd.builder()
                .id(IdUtils.nextId())
                .username("admin")
                .email(email)
                .password(password)
                .firstName("Super")
                .lastName("Admin")
                .tfaEnabled(false)
                .verified(true)
                .build();
    }

    public SearchResponse searchResponseFrom(User user) {
        if (user == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return SearchResponse.builder()
                .username(user.getUsername())
                .emailAddress(user.getEmail())
                .password(user.getPassword())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .address(user.getAddress())
                .phoneNumber(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }
}
