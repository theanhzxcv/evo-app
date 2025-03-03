package com.evo.storage.application.service.impl.query;

import com.evo.storage.application.dto.requests.FileSearchRequest;
import com.evo.storage.application.dto.responses.FileResponse;
import com.evo.storage.application.exception.AppException;
import com.evo.storage.application.exception.ErrorCode;
import com.evo.storage.application.mapper.FileDomainMapper;
import com.evo.storage.application.service.FileQueryService;
import com.evo.storage.domain.FileSt;
import com.evo.storage.domain.query.FileSearchQuery;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import com.evo.storage.infrastructure.persistence.mapper.FileEntityMapperImpl;
import com.evo.storage.infrastructure.persistence.repository.FileEntityRepository;
import com.evo.storage.infrastructure.persistence.repository.FileEntityRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicFileQueryServiceImpl implements FileQueryService {

    private final FileDomainMapper fileDomainMapper;
    private final FileEntityMapperImpl fileEntityMapper;
    private final FileEntityRepository fileEntityRepository;
    private final FileEntityRepositoryCustom fileEntityRepositoryCustom;

    @Override
    public FileResponse findByFileName(String fileName) {
        FileSt fileSt = fileEntityRepository.findByOriginalNameAndNotDeleted(fileName)
                .map(fileEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        if (fileSt.getVisibility().equalsIgnoreCase("private")) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return fileDomainMapper.responseFrom(fileSt);
    }

    @Override
    public Page<FileResponse> search(FileSearchRequest request) {
        FileSearchQuery query = fileDomainMapper.searchFrom(request);
        long total = fileEntityRepositoryCustom.count(query);
        if (total == 0) {
            return Page.empty();
        }
        List<FileResponse> fileResponses = fileEntityRepositoryCustom.publicSearch(query).stream()
                .map(file -> fileDomainMapper.responseFrom(fileEntityMapper.toDomain(file)))
                .toList();

        return new PageImpl<>(fileResponses, PageRequest.of(query.getPageIndex(), query.getPageSize()), total);
    }
}
