package com.evo.iam.infrastructure.persistence.mapper;

import com.evo.iam.domain.Permission;
import com.evo.iam.infrastructure.persistence.entities.PermissionEntity;
import com.evo.support.DomainEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PermissionEntityMapperImpl implements DomainEntityMapper<Permission, PermissionEntity> {

    @Override
    public Permission toDomain(PermissionEntity entity) {
        return Optional.ofNullable(entity)
                .map(permissionEntity -> Permission.builder()
                        .id(permissionEntity.getId())
                        .name(permissionEntity.getName())
                        .resource(permissionEntity.getResource())
                        .scope(permissionEntity.getScope())
                        .deleted(permissionEntity.getDeleted())
                        .createdBy(permissionEntity.getCreatedBy())
                        .createdAt(permissionEntity.getCreatedAt())
                        .lastModifiedBy(permissionEntity.getLastModifiedBy())
                        .lastModifiedAt(permissionEntity.getLastModifiedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public PermissionEntity toEntity(Permission domain) {
        return Optional.ofNullable(domain)
                .map(permissionDomain -> PermissionEntity.builder()
                        .id(permissionDomain.getId())
                        .name(permissionDomain.getName())
                        .resource(permissionDomain.getResource())
                        .scope(permissionDomain.getScope())
                        .deleted(permissionDomain.getDeleted())
                        .createdBy(permissionDomain.getCreatedBy())
                        .createdAt(permissionDomain.getCreatedAt())
                        .lastModifiedBy(permissionDomain.getLastModifiedBy())
                        .lastModifiedAt(permissionDomain.getLastModifiedAt())
                        .build())
                .orElse(null);
    }
}
