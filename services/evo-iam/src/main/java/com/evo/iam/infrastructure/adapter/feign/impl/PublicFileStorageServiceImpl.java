package com.evo.iam.infrastructure.adapter.feign.impl;

import com.evo.iam.application.dto.request.FileSearchRequest;
import com.evo.iam.application.dto.request.FileUpdateRequest;
import com.evo.iam.application.dto.request.ImageViewRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.api.PageApiResponse;
import com.evo.iam.application.dto.response.FileResponse;
import com.evo.iam.infrastructure.adapter.feign.PublicFileClient;
import com.evo.iam.infrastructure.adapter.feign.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicFileStorageServiceImpl implements FileStorageService {

    private final PublicFileClient publicFileClient;

    @Override
    public ApiResponse<FileResponse> upload(MultipartFile file, String description) {
        return publicFileClient.uploadFile(file, description);
    }

    @Override
    public ApiResponse<List<FileResponse>> uploadMultipleFiles(List<MultipartFile> files, String description) {
        return publicFileClient.uploadMultipleFiles(files, description);
    }

    @Override
    public ApiResponse<FileResponse> getFileByName(String fileName) {
        return publicFileClient.getFileByName(fileName);
    }

    @Override
    public PageApiResponse<FileResponse> search(FileSearchRequest fileSearchRequest) {
        return publicFileClient.searchFile(fileSearchRequest);
    }

    @Override
    public ApiResponse<FileResponse> update(String fileName, FileUpdateRequest fileUpdateRequest) {
        return publicFileClient.updateFile(fileName, fileUpdateRequest);
    }

    @Override
    public ApiResponse<FileResponse> delete(String fileName) {
        return publicFileClient.deleteFile(fileName);
    }

    @Override
    public ApiResponse<FileResponse> undelete(String fileName) {
        return publicFileClient.undeleteFile(fileName);
    }

    @Override
    public ResponseEntity<byte[]> viewImage(String image, ImageViewRequest request) {
        return null;
    }
}
