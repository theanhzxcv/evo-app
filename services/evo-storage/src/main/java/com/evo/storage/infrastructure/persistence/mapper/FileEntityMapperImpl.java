package com.evo.storage.infrastructure.persistence.mapper;

import com.evo.storage.domain.FileSt;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import com.evo.support.DomainEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FileEntityMapperImpl implements DomainEntityMapper<FileSt, FileEntity> {

    @Override
    public FileSt toDomain(FileEntity entity) {
        return Optional.ofNullable(entity)
                .map(fileEntity -> FileSt.builder()
                        .id(fileEntity.getId())
                        .originalName(fileEntity.getOriginalName())
                        .hashNameMd5(fileEntity.getHashNameMd5())
                        .path(fileEntity.getPath())
                        .type(fileEntity.getType())
                        .contentType(fileEntity.getContentType())
                        .extension(fileEntity.getExtension())
                        .owner(fileEntity.getOwner())
                        .description(fileEntity.getDescription())
                        .visibility(fileEntity.getVisibility())
                        .deleted(fileEntity.getDeleted())
                        .createdBy(fileEntity.getCreatedBy())
                        .createdAt(fileEntity.getCreatedAt())
                        .lastModifiedBy(fileEntity.getLastModifiedBy())
                        .lastModifiedAt(fileEntity.getLastModifiedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public FileEntity toEntity(FileSt domain) {
        return Optional.ofNullable(domain)
                .map(fileStDomain -> FileEntity.builder()
                        .id(fileStDomain.getId())
                        .originalName(fileStDomain.getOriginalName())
                        .hashNameMd5(fileStDomain.getHashNameMd5())
                        .path(fileStDomain.getPath())
                        .type(fileStDomain.getType())
                        .contentType(fileStDomain.getContentType())
                        .extension(fileStDomain.getExtension())
                        .owner(fileStDomain.getOwner())
                        .description(fileStDomain.getDescription())
                        .visibility(fileStDomain.getVisibility())
                        .deleted(fileStDomain.getDeleted())
                        .createdBy(fileStDomain.getCreatedBy())
                        .createdAt(fileStDomain.getCreatedAt())
                        .lastModifiedBy(fileStDomain.getLastModifiedBy())
                        .lastModifiedAt(fileStDomain.getLastModifiedAt())
                        .build())
                .orElse(null);
    }
}
