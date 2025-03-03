package com.evo.iam.infrastructure.repository;

import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.domain.Permission;
import com.evo.iam.domain.repository.PermissionDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.PermissionEntity;
import com.evo.iam.infrastructure.persistence.mapper.PermissionEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import com.evo.support.AbstractDomainRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PermissionDomainRepositoryImpl
        extends AbstractDomainRepository<Permission, PermissionEntity, UUID>
        implements PermissionDomainRepository {

    protected PermissionDomainRepositoryImpl(PermissionEntityRepository repository,
                                             PermissionEntityMapperImpl mapper) {
        super(repository, mapper);
    }

    @Override
    public Permission getById(UUID id) {
        return this.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
    }

    @Override
    public void existsById(UUID uuid) {}
}
