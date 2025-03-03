package com.evo.iam.presentation.rest;

import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.FileImportResponse;
import com.evo.iam.application.service.impl.command.ExcelCmdServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files/excel")
@Tag(name = "Excel")
public class ExcelManagementController {
    private final ExcelCmdServiceImpl excelManagementServiceImpl;

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<FileImportResponse>> importUserData (@RequestParam("file") MultipartFile file) {
        List<FileImportResponse> fileImportResponse = excelManagementServiceImpl.importUserData(file);
        return ApiResponse.of(fileImportResponse).success("Import successfully.");
    }

    @PostMapping(value = "/export")
    public ApiResponse<String> exportUserData (@RequestParam(value = "keyword") String keyword)
            throws IOException {
        String exportedToExcelFile = excelManagementServiceImpl.exportUserData(keyword);
        return ApiResponse.of(exportedToExcelFile).success("Export successfully.");
    }
}
