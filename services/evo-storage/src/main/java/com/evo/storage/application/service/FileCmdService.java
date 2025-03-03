package com.evo.storage.application.service;

import com.evo.storage.application.dto.requests.FileUpdateRequest;
import com.evo.storage.application.dto.requests.ImageViewRequest;
import com.evo.storage.application.dto.responses.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileCmdService {
    FileResponse upload(MultipartFile file, String description) throws IOException;

    List<FileResponse> uploadMultipleFile(List<MultipartFile> files,
                                          String description) throws IOException;

    String uploadProfileImage(MultipartFile file) throws IOException;

    byte[] viewImage(String image, ImageViewRequest request) throws IOException;

    FileResponse update(String fileName, FileUpdateRequest request);

    File download(String fileName) throws  IOException;

    FileResponse delete(String fileName);

    FileResponse undelete(String fileName);

}
