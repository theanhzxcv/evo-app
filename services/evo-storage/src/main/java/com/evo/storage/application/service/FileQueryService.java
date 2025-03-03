package com.evo.storage.application.service;

import com.evo.storage.application.dto.requests.FileSearchRequest;
import com.evo.storage.application.dto.responses.FileResponse;
import org.springframework.data.domain.Page;

public interface FileQueryService {
    FileResponse findByFileName(String fileName);

    Page<FileResponse> search(FileSearchRequest request);
}
