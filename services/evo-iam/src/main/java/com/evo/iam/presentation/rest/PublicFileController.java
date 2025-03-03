package com.evo.iam.presentation.rest;

import com.evo.iam.application.dto.request.FileSearchRequest;
import com.evo.iam.application.dto.request.FileUpdateRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.api.PageApiResponse;
import com.evo.iam.application.dto.response.FileResponse;
import com.evo.iam.infrastructure.adapter.feign.impl.PublicFileStorageServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/files")
@Tag(name = "PublicFileStorage")
public class PublicFileController {
    private final PublicFileStorageServiceImpl publicFileStorageServiceImpl;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {

        return publicFileStorageServiceImpl.upload(file, description);
    }

    @PostMapping(path = "/multiple-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<FileResponse>> uploadMultipleFile(
            @RequestParam("file") List<MultipartFile> files,
            @RequestParam("description") String description) {

        return publicFileStorageServiceImpl.uploadMultipleFiles(files, description);
    }

    @GetMapping("/{fileName}")
    public ApiResponse<FileResponse> getFileByName(@PathVariable("fileName") String fileName) {
        ApiResponse<FileResponse> fileFound = publicFileStorageServiceImpl.getFileByName(fileName);

        if (fileFound == null) {
            return ApiResponse.fail(HttpStatus.NOT_FOUND, "File not found with: " + fileFound);
        }
        return fileFound;
    }

    @GetMapping("/search/keyword")
    public PageApiResponse<FileResponse> searchFile(
            @ParameterObject @Valid FileSearchRequest fileSearchRequest) {
        PageApiResponse<FileResponse> fileFound = publicFileStorageServiceImpl.search(fileSearchRequest);

        return PageApiResponse.of(fileFound.getData(),
                fileFound.getPage().getPageIndex(),
                fileFound.getPage().getPageSize(),
                fileFound.getPage().getTotal());
    }

    @PutMapping("/{fileName}")
    public ApiResponse<FileResponse> updateFile(
            @PathVariable("fileName") String fileName,
            @ParameterObject @Valid FileUpdateRequest fileUpdateRequest) {

        return publicFileStorageServiceImpl.update(fileName, fileUpdateRequest);
    }

    @DeleteMapping("/{fileName}")
    public ApiResponse<FileResponse> deleteFile(@PathVariable("fileName") String fileName) {
        return publicFileStorageServiceImpl.delete(fileName);
    }

    @PutMapping("/undelete/{fileName}")
    public ApiResponse<FileResponse> undeleteFile(@PathVariable("fileName") String fileName) {
        return publicFileStorageServiceImpl.undelete(fileName);
    }
}
