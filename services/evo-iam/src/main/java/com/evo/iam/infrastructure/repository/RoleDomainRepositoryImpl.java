package com.evo.iam.infrastructure.repository;

import com.evo.iam.domain.Role;
import com.evo.iam.domain.repository.RoleDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.RoleEntity;
import com.evo.iam.infrastructure.persistence.entities.RolePermissionEntity;
import com.evo.iam.infrastructure.persistence.mapper.RoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.RolePermissionEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.RolePermissionEntityRepository;
import com.evo.support.AbstractDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class RoleDomainRepositoryImpl
        extends AbstractDomainRepository<Role, RoleEntity, UUID>
        implements RoleDomainRepository {

    private final RoleEntityMapperImpl roleEntityMapper;
    private final RolePermissionEntityMapperImpl rolePermissionEntityMapper;
    private final RoleEntityRepository roleEntityRepository;
    private final RolePermissionEntityRepository rolePermissionEntityRepository;

    protected RoleDomainRepositoryImpl(RoleEntityRepository roleEntityRepository,
                                       RolePermissionEntityRepository rolePermissionEntityRepository,
                                       RoleEntityMapperImpl roleEntityMapper,
                                       RolePermissionEntityMapperImpl rolePermissionEntityMapper) {
        super(roleEntityRepository, roleEntityMapper);
        this.roleEntityMapper = roleEntityMapper;
        this.roleEntityRepository = roleEntityRepository;
        this.rolePermissionEntityRepository = rolePermissionEntityRepository;
        this.rolePermissionEntityMapper = rolePermissionEntityMapper;
    }

    @Override
    public Role save(Role role) {
        RoleEntity roleEntity = roleEntityMapper.toEntity(role);
        this.roleEntityRepository.save(roleEntity);

        if (!CollectionUtils.isEmpty(role.getRolePermissions())) {
            List<RolePermissionEntity> rolePermissionEntities = role.getRolePermissions().stream()
                    .map(rolePermissionEntityMapper::toEntity)
                    .toList();
            rolePermissionEntityRepository.saveAll(rolePermissionEntities);
        }

        return role;
    }

    @Override
    public Role getById(UUID uuid) {
        return null;
    }

    @Override
    public void existsById(UUID uuid) {

    }
}
