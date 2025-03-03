package com.evo.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Response<T> implements Serializable {
    protected T data;
    private boolean success;
    private int code;
    private String message;
    private long timestamp;
    @JsonIgnore
    private RuntimeException exception;

    public static <T> Response<T> fail(RuntimeException exception) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setException(exception);
        return response;
    }

    public Response<T> success() {
        success = true;
        code = 200;
        return this;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }
}