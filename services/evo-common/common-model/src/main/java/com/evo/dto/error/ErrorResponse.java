package com.evo.dtos.error;

import com.evo.dtos.responses.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> extends ApiResponse<T> {
    private String error;

    @Builder
    public ErrorResponse(int code, String message, T data, String error) {
        this.setData(data);
        this.setCode(code);
        this.setMessage(message);
        this.setSuccess(false);
        this.error = error;
    }
}
