package com.evo.storage.infrastructure.repository;

import com.evo.storage.domain.FileSt;
import com.evo.storage.domain.repository.FileDomainRepository;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import com.evo.storage.infrastructure.persistence.mapper.FileEntityMapperImpl;
import com.evo.storage.infrastructure.persistence.repository.FileEntityRepository;
import com.evo.support.AbstractDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public class FileDomainRepositoryImpl
        extends AbstractDomainRepository<FileSt, FileEntity, UUID>
        implements FileDomainRepository {

    protected FileDomainRepositoryImpl(FileEntityRepository fileEntityRepository,
                                       FileEntityMapperImpl fileEntityMapper) {
        super(fileEntityRepository, fileEntityMapper);
    }

    @Override
    public FileSt getById(UUID id) {
        return this.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }

    @Override
    public void existsById(UUID uuid) {

    }
}
