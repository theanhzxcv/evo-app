package com.evo.dtos.error;

public interface ResponseError {
    String getName();

    String getMessage();

    int getStatus();

    default Integer getCode() {
        return 0;
    }
}
