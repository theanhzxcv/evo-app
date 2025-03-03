package com.evo.iam.infrastructure.adapter.feign;

import com.evo.config.FeignClientConfiguration;
import com.evo.iam.application.dto.request.FileSearchRequest;
import com.evo.iam.application.dto.request.FileUpdateRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.api.PageApiResponse;
import com.evo.iam.application.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(
        name = "public-storage-service",
        url = "${app.services.storage.public}",
        configuration = FeignClientConfiguration.class
)
public interface PublicFileClient {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadFile(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "description") String description);

    @PostMapping(path = "/multiple-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<List<FileResponse>> uploadMultipleFiles(
            @RequestPart(value = "files") List<MultipartFile> files,
            @RequestPart(value = "description", required = false) String description);

    @GetMapping(path = "/{fileName}")
    ApiResponse<FileResponse> getFileByName(@PathVariable(value = "fileName") String fileName);

    @GetMapping(path = "/search/keyword")
    PageApiResponse<FileResponse> searchFile(@SpringQueryMap FileSearchRequest fileSearchRequest);

    @PutMapping(path = "/{fileName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<FileResponse> updateFile(
            @PathVariable(value = "fileName") String fileName,
            @RequestBody FileUpdateRequest fileUpdateRequest);

    @DeleteMapping(path = "/{fileName}")
    ApiResponse<FileResponse> deleteFile(@PathVariable(value = "fileName") String fileName);

    @PutMapping(path = "/undelete/{fileName}")
    ApiResponse<FileResponse> undeleteFile(@PathVariable(value = "fileName") String fileName);

//    default ApiResponse<Object> fallbackMethod(String fileName, Throwable throwable) {
//        logger.info("Cannot get information from file {}, failure reason: {}", fileName, throwable.getMessage());
//
//        // Create and return a fallback ApiResponse<FileResponse> (could be an error response)
//        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR).fail("Error fetching file: " + fileName);
//    }

}

