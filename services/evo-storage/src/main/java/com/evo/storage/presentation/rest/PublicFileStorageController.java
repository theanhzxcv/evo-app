package com.evo.storage.presentation.rest;

import com.evo.storage.application.dto.requests.FileSearchRequest;
import com.evo.storage.application.dto.requests.FileUpdateRequest;
import com.evo.storage.application.dto.responses.api.ApiResponse;
import com.evo.storage.application.dto.responses.FileResponse;
import com.evo.storage.application.dto.responses.api.PageApiResponse;
import com.evo.storage.application.factory.FileCmdServiceFactory;
import com.evo.storage.application.factory.FileQueryServiceFactory;
import com.evo.storage.application.service.FileCmdService;
import com.evo.storage.application.service.impl.command.PublicFileCmdServiceImpl;
import com.evo.storage.application.service.FileQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/files")
@Tag(name = "PublicFileManagement")
public class PublicFileStorageController {
    private final PublicFileCmdServiceImpl publicFileCmdService;
    private final FileCmdServiceFactory fileCmdServiceFactory;
    private final FileQueryServiceFactory fileQueryServiceFactory;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileResponse> uploadFile(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            FileResponse uploadedFile = fileCmdService.upload(file, description);
            return ApiResponse.of(uploadedFile).success("File uploaded.");
        } catch (IOException e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }

    @PostMapping(path = "/multiple-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<FileResponse>> uploadMultipleFiles(
            @RequestParam(value = "files") List<MultipartFile> files,
            @RequestParam(value = "description", required = false) String description) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            List<FileResponse> uploadedFile = fileCmdService.uploadMultipleFile(files,
                    description);
            return ApiResponse.of(uploadedFile).success("Multiple files upload.");
        } catch (IOException e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }

    @GetMapping(path = "/{fileName}")
    public ApiResponse<FileResponse> getFileByName(@PathVariable(value = "fileName") String fileName) {
        FileQueryService fileQueryService = fileQueryServiceFactory.getService();

        try {
            FileResponse fileFound = fileQueryService.findByFileName(fileName);
            return ApiResponse.of(fileFound).success("File found with name: " + fileName);
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.NOT_FOUND, "");
        }
    }

    @GetMapping(path = "/search/keyword")
    public PageApiResponse<FileResponse> searchFile(@ParameterObject FileSearchRequest fileSearchRequest) {
        FileQueryService fileQueryService = fileQueryServiceFactory.getService();
        Page<FileResponse> fileMatched = fileQueryService.search(fileSearchRequest);

        try {
            if (fileMatched.isEmpty()) {
                return PageApiResponse.failPaging("");
            }
            return PageApiResponse.of(fileMatched.getContent(),
                    fileMatched.getNumber(),
                    fileMatched.getSize(),
                    fileMatched.getTotalElements());
        } catch (Exception e) {
            return PageApiResponse.failPaging(e.getMessage());
        }
    }

    @PutMapping(path = "/{fileName}")
    public ApiResponse<FileResponse> updateFile(
            @PathVariable("fileName") String fileName,
            @RequestBody FileUpdateRequest fileUpdateRequest) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            FileResponse updatedFile = fileCmdService.update(fileName, fileUpdateRequest);
            return ApiResponse.of(updatedFile).success("File updated.");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }

    }

    @DeleteMapping(path = "/{fileName}")
    public ApiResponse<FileResponse> deleteFile(@PathVariable(value = "fileName") String fileName) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            FileResponse deletedFile = publicFileCmdService.delete(fileName);
            return ApiResponse.of(deletedFile).success("File deleted.");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }

    @PutMapping(path = "/undelete/{fileName}")
    public ApiResponse<FileResponse> undeleteFile(@PathVariable(value = "fileName") String fileName) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            FileResponse undeletedFile = fileCmdService.undelete(fileName);
            return ApiResponse.of(undeletedFile).success("File deleted.");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }
}
