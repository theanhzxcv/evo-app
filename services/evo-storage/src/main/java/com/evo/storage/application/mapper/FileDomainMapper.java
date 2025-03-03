package com.evo.storage.application.mapper;

import com.evo.storage.application.dto.requests.FileSearchRequest;
import com.evo.storage.application.dto.requests.FileUpdateRequest;
import com.evo.storage.application.dto.responses.FileResponse;
import com.evo.storage.application.exception.AppException;
import com.evo.storage.application.exception.ErrorCode;
import com.evo.storage.domain.FileSt;
import com.evo.storage.domain.command.FileCreationCmd;
import com.evo.storage.domain.command.FileUpdateCmd;
import com.evo.storage.domain.query.FileSearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileDomainMapper {

    public FileCreationCmd createFrom(MultipartFile file, String description) {

        return FileCreationCmd.builder()
                .file(file)
                .description(description)
                .build();
    }

    public FileCreationCmd uploadAvatarFrom(MultipartFile file) {

        return FileCreationCmd.builder()
                .file(file)
                .description("Profile's image")
                .build();
    }

    public FileResponse responseFrom(FileSt fileSt) {
        if (fileSt == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return FileResponse.builder()
                .originalName(fileSt.getOriginalName())
                .description(fileSt.getDescription())
                .type(fileSt.getType())
                .extension(fileSt.getExtension())
                .contentType(fileSt.getContentType())
                .owner(fileSt.getOwner())
                .visibility(fileSt.getVisibility())
                .deleted(fileSt.getDeleted())
                .createdBy(fileSt.getCreatedBy())
                .createdAt(fileSt.getCreatedAt())
                .lastModifiedBy(fileSt.getLastModifiedBy())
                .lastModifiedAt(fileSt.getLastModifiedAt())
                .build();
    }

    public void updateFrom(FileUpdateCmd fileUpdateCmd, FileUpdateRequest fileUpdateRequest) {
        if (fileUpdateRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        fileUpdateCmd.setDescription(fileUpdateRequest.getDescription());
        fileUpdateCmd.setOwner(fileUpdateRequest.getOwner());
        if ("Yes".equalsIgnoreCase(fileUpdateRequest.getSetPublic())) {
            fileUpdateCmd.setVisibility("PUBLIC");
        } else {
            fileUpdateCmd.setVisibility("PRIVATE");
        }
    }

    public FileSearchQuery searchFrom(FileSearchRequest fileSearchRequest) {
        if (fileSearchRequest == null) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }

        return FileSearchQuery.builder()
                .keyword(fileSearchRequest.getKeyword())
                .pageIndex(fileSearchRequest.getPageIndex())
                .pageSize(fileSearchRequest.getPageSize())
                .build();
    }
}
