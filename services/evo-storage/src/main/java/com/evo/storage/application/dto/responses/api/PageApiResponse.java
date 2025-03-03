package com.evo.storage.application.dto.responses.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageApiResponse<T> extends ApiResponse<List<T>> {
    private PageableResponse page;

    public PageApiResponse() {
        super();
    }

    public PageApiResponse(List<T> data, int pageIndex, int pageSize, long total) {
        super();
        this.page = new PageableResponse(pageIndex, pageSize, total);
        this.data = data;
        this.success();
    }

    public static <T> PageApiResponse<T> of(List<T> data, int pageIndex, int pageSize, long total) {
        return new PageApiResponse<>(data, pageIndex, pageSize, total);
    }

    public static <T> PageApiResponse<T> failPaging(String message) {
        PageApiResponse<T> response = new PageApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(400);
        return response;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PageableResponse implements Serializable {
        private int pageIndex;
        private int pageSize;
        private long total;

        public PageableResponse(int pageIndex, int pageSize, long total) {
            this.pageIndex = pageIndex;
            this.pageSize = pageSize;
            this.total = total;
        }
    }
}
