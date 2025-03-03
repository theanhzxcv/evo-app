package com.evo.iam.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // System Errors
    INTERNAL_SERVER_ERROR("An unexpected error occurred. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR),
    MAPPER_ERROR("Failed to map data due to invalid input.", HttpStatus.UNPROCESSABLE_ENTITY),

    // Authentication & Authorization
    INVALID_REFRESH_TOKEN("Invalid or expired refresh token. Please re-authenticate.", HttpStatus.UNAUTHORIZED),
    BLACKLISTED_REFRESH_TOKEN("This refresh token has been blacklisted.", HttpStatus.FORBIDDEN),
    SESSION_EXPIRED("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED),
    AUTHENTICATION_FAILED("Invalid email or password.", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_PERMISSIONS("Access denied. You do not have the required permissions.", HttpStatus.FORBIDDEN),

    // User Management
    USER_ALREADY_EXISTS("Email or username already in use.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("User not found.", HttpStatus.NOT_FOUND),
    USER_DEACTIVATED("Your account has been deactivated or banned.", HttpStatus.FORBIDDEN),
    USER_DELETED("User has been deleted and cannot be accessed.", HttpStatus.BAD_REQUEST),
    USER_RESTORED("User has been restored successfully.", HttpStatus.OK),
    REGISTRATION_FAILED("User registration failed.", HttpStatus.BAD_REQUEST),
    UPDATE_USER_FAILED("Failed to update user details.", HttpStatus.BAD_REQUEST),
    INCOMPLETE_PROFILE("Profile incomplete. Update your details to activate your account.", HttpStatus.BAD_REQUEST),

    // Password & Security
    PASSWORD_POLICY_VIOLATION("Password must be at least 8 characters with a number, uppercase letter, and special character.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("Passwords do not match.", HttpStatus.BAD_REQUEST),
    INVALID_OTP("Invalid OTP. Please try again.", HttpStatus.BAD_REQUEST),
    ALREADY_VERIFIED("User is already verified.", HttpStatus.CONFLICT),

    // Roles & Permissions
    ROLE_ALREADY_EXISTS("Role name already exists.", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND("Role not found.", HttpStatus.NOT_FOUND),
    ROLE_DELETED("Role has been deleted and cannot be accessed.", HttpStatus.BAD_REQUEST),
    ROLE_RESTORED("Role has been restored successfully.", HttpStatus.OK),

    PERMISSION_ALREADY_EXISTS("Permission name already exists.", HttpStatus.CONFLICT),
    PERMISSION_NOT_FOUND("Permission not found.", HttpStatus.NOT_FOUND),
    PERMISSION_DELETED("Permission has been deleted and cannot be accessed.", HttpStatus.BAD_REQUEST),
    PERMISSION_RESTORED("Permission has been restored successfully.", HttpStatus.OK),

    // Validation Errors
    FIELD_MISSING("Required fields are missing.", HttpStatus.BAD_REQUEST),
    INVALID_DATE_OF_BIRTH("Date of birth must be in the past.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
