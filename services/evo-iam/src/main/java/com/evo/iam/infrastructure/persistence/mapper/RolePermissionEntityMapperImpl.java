package com.evo.iam.infrastructure.persistence.mapper;

import com.evo.iam.domain.RolePermission;
import com.evo.iam.infrastructure.persistence.entities.RolePermissionEntity;
import com.evo.support.DomainEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RolePermissionEntityMapperImpl implements DomainEntityMapper<RolePermission, RolePermissionEntity> {

    @Override
    public RolePermission toDomain(RolePermissionEntity entity) {
        return Optional.ofNullable(entity)
                .map(rolePermissionEntity -> RolePermission.builder()
                        .id(rolePermissionEntity.getId())
                        .roleId(rolePermissionEntity.getRoleId())
                        .permissionId(rolePermissionEntity.getPermissionId())
                        .deleted(rolePermissionEntity.getDeleted())
                        .createdBy(rolePermissionEntity.getCreatedBy())
                        .createdAt(rolePermissionEntity.getCreatedAt())
                        .lastModifiedBy(rolePermissionEntity.getLastModifiedBy())
                        .lastModifiedAt(rolePermissionEntity.getLastModifiedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public RolePermissionEntity toEntity(RolePermission domain) {
        return Optional.ofNullable(domain)
                .map(rolePermissionDomain -> RolePermissionEntity.builder()
                        .id(rolePermissionDomain.getId())
                        .roleId(rolePermissionDomain.getRoleId())
                        .permissionId(rolePermissionDomain.getPermissionId())
                        .deleted(rolePermissionDomain.getDeleted())
                        .createdBy(rolePermissionDomain.getCreatedBy())
                        .createdAt(rolePermissionDomain.getCreatedAt())
                        .lastModifiedBy(rolePermissionDomain.getLastModifiedBy())
                        .lastModifiedAt(rolePermissionDomain.getLastModifiedAt())
                        .build())
                .orElse(null);
    }
}
