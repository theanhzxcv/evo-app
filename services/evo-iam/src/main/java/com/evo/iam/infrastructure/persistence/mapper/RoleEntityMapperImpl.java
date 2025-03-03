package com.evo.iam.infrastructure.persistence.mapper;

import com.evo.iam.domain.Role;
import com.evo.iam.infrastructure.persistence.entities.RoleEntity;
import com.evo.support.DomainEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleEntityMapperImpl implements DomainEntityMapper<Role, RoleEntity> {

    @Override
    public Role toDomain(RoleEntity entity) {
        return Optional.ofNullable(entity)
                .map(roleEntity -> Role.builder()
                        .id(roleEntity.getId())
                        .name(roleEntity.getName())
                        .root(roleEntity.getRoot())
                        .deleted(roleEntity.getDeleted())
                        .createdBy(roleEntity.getCreatedBy())
                        .createdAt(roleEntity.getCreatedAt())
                        .lastModifiedBy(roleEntity.getLastModifiedBy())
                        .lastModifiedAt(roleEntity.getLastModifiedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public RoleEntity toEntity(Role domain) {
        return Optional.ofNullable(domain)
                .map(roleDomain -> RoleEntity.builder()
                        .id(roleDomain.getId())
                        .name(roleDomain.getName())
                        .root(roleDomain.getRoot())
                        .deleted(roleDomain.getDeleted())
                        .createdBy(roleDomain.getCreatedBy())
                        .createdAt(roleDomain.getCreatedAt())
                        .lastModifiedBy(roleDomain.getLastModifiedBy())
                        .lastModifiedAt(roleDomain.getLastModifiedAt())
                        .build())
                .orElse(null);
    }
}
