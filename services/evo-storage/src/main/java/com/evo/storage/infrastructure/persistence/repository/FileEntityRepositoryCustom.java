package com.evo.storage.infrastructure.persistence.repository;

import com.evo.storage.application.dto.requests.FileSearchRequest;
import com.evo.storage.domain.FileSt;
import com.evo.storage.domain.query.FileSearchQuery;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;

import java.io.File;
import java.util.List;

public interface FileEntityRepositoryCustom {
    List<FileEntity> search(FileSearchQuery query);

    List<FileEntity> publicSearch(FileSearchQuery query);

    Long count(FileSearchQuery query);
}
