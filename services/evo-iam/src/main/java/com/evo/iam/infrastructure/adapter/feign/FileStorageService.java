package com.evo.iam.infrastructure.adapter.feign;

import com.evo.iam.application.dto.request.FileSearchRequest;
import com.evo.iam.application.dto.request.FileUpdateRequest;
import com.evo.iam.application.dto.request.ImageViewRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.api.PageApiResponse;
import com.evo.iam.application.dto.response.FileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    ApiResponse<FileResponse> upload(MultipartFile file, String description);

    ApiResponse<List<FileResponse>> uploadMultipleFiles(List<MultipartFile> files, String description);

    ApiResponse<FileResponse> getFileByName(String fileName);

    PageApiResponse<FileResponse> search(FileSearchRequest request);

    ApiResponse<FileResponse> update(String fileName, FileUpdateRequest request);

    ApiResponse<FileResponse> delete(String fileName);

    ApiResponse<FileResponse> undelete(String fileName);

    ResponseEntity<byte[]> viewImage(String image, ImageViewRequest request);

}