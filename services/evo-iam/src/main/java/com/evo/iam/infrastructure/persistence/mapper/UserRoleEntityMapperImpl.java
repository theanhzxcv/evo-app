package com.evo.iam.infrastructure.persistence.mapper;

import com.evo.iam.domain.Role;
import com.evo.iam.domain.UserRole;
import com.evo.iam.infrastructure.persistence.entities.UserRoleEntity;
import com.evo.support.DomainEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRoleEntityMapperImpl implements DomainEntityMapper<UserRole, UserRoleEntity> {

    @Override
    public UserRole toDomain(UserRoleEntity entity) {
        return Optional.ofNullable(entity)
                .map(userRoleEntity -> UserRole.builder()
                        .id(userRoleEntity.getId())
                        .userId(userRoleEntity.getUserId())
                        .roleId(userRoleEntity.getRoleId())
                        .deleted(userRoleEntity.getDeleted())
                        .createdBy(userRoleEntity.getCreatedBy())
                        .createdAt(userRoleEntity.getCreatedAt())
                        .lastModifiedBy(userRoleEntity.getLastModifiedBy())
                        .lastModifiedAt(userRoleEntity.getLastModifiedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public UserRoleEntity toEntity(UserRole domain) {
        return Optional.ofNullable(domain)
                .map(userRoleDomain -> UserRoleEntity.builder()
                        .id(userRoleDomain.getId())
                        .userId(userRoleDomain.getUserId())
                        .roleId(userRoleDomain.getRoleId())
                        .deleted(userRoleDomain.getDeleted())
                        .createdBy(userRoleDomain.getCreatedBy())
                        .createdAt(userRoleDomain.getCreatedAt())
                        .lastModifiedBy(userRoleDomain.getLastModifiedBy())
                        .lastModifiedAt(userRoleDomain.getLastModifiedAt())
                        .build())
                .orElse(null);
    }
}
