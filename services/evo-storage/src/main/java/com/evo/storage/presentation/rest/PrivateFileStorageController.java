package com.evo.storage.presentation.rest;

import com.evo.storage.application.dto.requests.FileSearchRequest;
import com.evo.storage.application.dto.requests.FileUpdateRequest;
import com.evo.storage.application.dto.requests.ImageViewRequest;
import com.evo.storage.application.dto.responses.api.ApiResponse;
import com.evo.storage.application.dto.responses.FileResponse;
import com.evo.storage.application.dto.responses.api.PageApiResponse;
import com.evo.storage.application.factory.FileCmdServiceFactory;
import com.evo.storage.application.factory.FileQueryServiceFactory;
import com.evo.storage.application.service.FileCmdService;
import com.evo.storage.application.service.FileQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/files")
@Tag(name = "PrivateFileManagement")
public class PrivateFileStorageController {
    private final FileCmdServiceFactory fileCmdServiceFactory;
    private final FileQueryServiceFactory fileQueryServiceFactory;

    @PreAuthorize("hasPermission('File','Create')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FileResponse> uploadFile(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description
    ) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            FileResponse uploadedFile = fileCmdService.upload(file, description);
            return ApiResponse.of(uploadedFile).success("File uploaded.");
        } catch (IOException e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }

    @PreAuthorize("hasPermission('File','Create')")
    @PostMapping(path = "/multiple-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<FileResponse>> uploadMultipleFiles(
            @RequestParam(value = "files") List<MultipartFile> files,
            @RequestParam(value = "description", required = false) String description) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            List<FileResponse> uploadedFile = fileCmdService.uploadMultipleFile(files,
                    description);
            return ApiResponse.of(uploadedFile).success("Multiple files uploaded.");
        } catch (IOException e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }

    @PreAuthorize("hasPermission('File','Create')")
    @PostMapping(path = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> uploadProfileImage(@RequestParam(value = "image") MultipartFile image) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            String uploadedImage = fileCmdService.uploadProfileImage(image);
            return ApiResponse.of(uploadedImage).success("Image uploaded");
        } catch (IOException e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }

    @PreAuthorize("hasPermission('File','Read')")
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

    @PreAuthorize("hasPermission('File','Read')")
    @GetMapping(path = "/search/keyword")
    public PageApiResponse<FileResponse> searchFile(@ParameterObject FileSearchRequest fileSearchRequest) {
        FileQueryService fileQueryService = fileQueryServiceFactory.getService();
        Page<FileResponse> fileMatched = fileQueryService.search(fileSearchRequest);

        try {
            if (fileMatched.isEmpty()) {
                return PageApiResponse.failPaging("File not found with: " + fileSearchRequest.getKeyword());
            }
            return PageApiResponse.of(fileMatched.getContent(),
                    fileMatched.getNumber(),
                    fileMatched.getSize(),
                    fileMatched.getTotalElements());
        } catch (Exception e) {
            return PageApiResponse.failPaging(e.getMessage());
        }
    }

    @PreAuthorize("hasPermission('File','Update')")
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

    @PreAuthorize("hasPermission('File','Read')")
    @GetMapping(path = "/{fileName}/download")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(value = "fileName") String filename) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            var fileToDownload = fileCmdService.download(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(Files.newInputStream(fileToDownload.toPath())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PreAuthorize("hasPermission('File','Read')")
    @GetMapping(path = "/view/{image}")
    public ResponseEntity<byte[]> viewImage(
            @PathVariable("image") String image,
            @ParameterObject ImageViewRequest imageViewRequest) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            byte[] imageBytes = fileCmdService.viewImage(image, imageViewRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasPermission('File','Delete')")
    @DeleteMapping(path = "/{fileName}")
    public ApiResponse<FileResponse> deleteFile(@PathVariable("fileName") String fileName) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            FileResponse deletedFile = fileCmdService.delete(fileName);
            return ApiResponse.of(deletedFile).success("File deleted");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }

    @PreAuthorize("hasPermission('File','Update')")
    @PutMapping(path = "/undelete/{fileName}")
    public ApiResponse<FileResponse> undeleteFile(@PathVariable("fileName") String fileName) {
        FileCmdService fileCmdService = fileCmdServiceFactory.getService();

        try {
            FileResponse undeletedFile = fileCmdService.undelete(fileName);
            return ApiResponse.of(undeletedFile).success("File deleted");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "");
        }
    }
}
