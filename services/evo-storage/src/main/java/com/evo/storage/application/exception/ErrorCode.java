package com.evo.storage.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // System errors
    INTERNAL_SERVER_ERROR("Unexpected error occurred! Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    MAPPER_ERROR("Mapping failed due to invalid or missing data!", HttpStatus.UNPROCESSABLE_ENTITY),

    // Authorization & Permission errors
    INSUFFICIENT_PERMISSIONS("Access denied. You do not have the required permissions.", HttpStatus.FORBIDDEN),
    ACCESS_DENIED("You do not have permission to access this file.", HttpStatus.FORBIDDEN),

    // File storage errors
    INVALID_FILE("The provided file is invalid or corrupted!", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND("The requested file does not exist or has been deleted!", HttpStatus.NOT_FOUND),
    FILE_ALREADY_EXISTS("A file with the same name already exists!", HttpStatus.CONFLICT),
    UNSUPPORTED_FILE_TYPE("The uploaded file type is not supported!", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    UNSUPPORTED_EXTENSION("The uploaded file has an unsupported extension!", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    FILE_UPLOAD_FAILED("File upload failed due to an internal error!", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("File deletion failed due to an internal error!", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DOWNLOAD_FAILED("File download failed due to an internal error!", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_ACCESS_DENIED("You do not have permission to access this file!", HttpStatus.FORBIDDEN),
    STORAGE_LIMIT_EXCEEDED("Storage limit exceeded. Cannot upload more files!", HttpStatus.INSUFFICIENT_STORAGE),
    INVALID_FILE_NAME("The provided file name is invalid or contains restricted characters!", HttpStatus.BAD_REQUEST),
    FILE_ALREADY_DELETED("This file is already deleted!", HttpStatus.BAD_REQUEST),
    FILE_ALREADY_RESTORED("This file is not deleted and cannot be restored!", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
