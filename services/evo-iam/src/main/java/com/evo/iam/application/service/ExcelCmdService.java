package com.evo.iam.application.service;

import com.evo.iam.application.dto.response.FileImportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ExcelCmdService {

    List<FileImportResponse> importUserData(MultipartFile file) throws FileNotFoundException;

    String exportUserData(String keyword) throws IOException;
}
