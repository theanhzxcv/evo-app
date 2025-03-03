package com.evo.iam.application.dto.response.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    T data;
    private boolean success;
    private int code;
    private String message;
    private long timestamp;

    public ApiResponse() {
        this.timestamp = Instant.now().toEpochMilli();
    }

    public static <T> ApiResponse<T> of(T res) {
        return new ApiResponse<T>().data(res).success();
    }

    public static <T> ApiResponse<T> created(T res) {
        return new ApiResponse<T>().data(res).status(HttpStatus.CREATED);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<T>().success();
    }

    public static <T> ApiResponse<T> fail(HttpStatus status, String message) {
        return new ApiResponse<T>().status(status).fail(message);
    }

    public ApiResponse<T> data(T res) {
        this.data = res;
        return this;
    }

    public ApiResponse<T> success() {
        this.success = true;
        this.code = HttpStatus.OK.value();
        return this;
    }

    public ApiResponse<T> success(String message) {
        this.success = true;
        this.code = HttpStatus.OK.value();
        this.message = message;
        return this;
    }

    public ApiResponse<T> fail(String message) {
        this.success = false;
        this.message = message;
        return this;
    }

    public ApiResponse<T> status(HttpStatus status) {
        this.code = status.value();
        return this;
    }
}
