package com.evo.iam.infrastructure.adapter.feign.impl;

import com.evo.iam.application.dto.request.FileSearchRequest;
import com.evo.iam.application.dto.request.FileUpdateRequest;
import com.evo.iam.application.dto.request.ImageViewRequest;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.api.PageApiResponse;
import com.evo.iam.application.dto.response.FileResponse;
import com.evo.iam.infrastructure.adapter.feign.PrivateFileClient;
import com.evo.iam.infrastructure.adapter.feign.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateFileStorageServiceImpl implements FileStorageService {

    private final PrivateFileClient privateFileClient;

    @Override
    public ApiResponse<FileResponse> upload(MultipartFile file, String description) {
        return privateFileClient.uploadFile(file, description);
    }

    @Override
    public ApiResponse<List<FileResponse>> uploadMultipleFiles(List<MultipartFile> files, String description) {
        return privateFileClient.uploadMultipleFiles(files, description);
    }

    @Override
    public ApiResponse<FileResponse> getFileByName(String fileName) {
        return privateFileClient.getFileByName(fileName);
    }

    @Override
    public PageApiResponse<FileResponse> search(FileSearchRequest fileSearchRequest) {
        return privateFileClient.searchFile(fileSearchRequest);
    }

    @Override
    public ApiResponse<FileResponse> update(String fileName, FileUpdateRequest fileUpdateRequest) {
        return privateFileClient.updateFile(fileName, fileUpdateRequest);
    }

    @Override
    public ApiResponse<FileResponse> delete(String fileName) {
        return privateFileClient.deleteFile(fileName);
    }

    @Override
    public ApiResponse<FileResponse> undelete(String fileName) {
        return privateFileClient.undeleteFile(fileName);
    }

    @Override
    public ResponseEntity<byte[]> viewImage(String image, ImageViewRequest request) {
        return privateFileClient.viewImage(image, request);
    }

}
