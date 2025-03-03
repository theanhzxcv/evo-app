package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.RoleCreationRequest;
import com.evo.iam.application.dto.request.RoleUpdateRequest;
import com.evo.iam.application.dto.response.RoleResponse;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.RoleDomainMapper;
import com.evo.iam.application.mapper.RolePermissionDomainMapper;
import com.evo.iam.application.service.RoleCmdService;
import com.evo.iam.domain.Permission;
import com.evo.iam.domain.Role;
import com.evo.iam.domain.RolePermission;
import com.evo.iam.domain.command.RoleCreationCmd;
import com.evo.iam.domain.command.RolePermissionCmd;
import com.evo.iam.domain.command.RoleUpdateCmd;
import com.evo.iam.domain.repository.RoleDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.PermissionEntity;
import com.evo.iam.infrastructure.persistence.entities.RoleEntity;
import com.evo.iam.infrastructure.persistence.mapper.PermissionEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.RoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.RolePermissionEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.RolePermissionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleCmdServiceImpl implements RoleCmdService {

    private final RoleDomainMapper roleDomainMapper;
    private final RoleDomainRepository roleDomainRepository;
    private final RoleEntityMapperImpl roleEntityMapper;
    private final RoleEntityRepository roleEntityRepository;
    private final PermissionEntityMapperImpl permissionEntityMapper;
    private final PermissionEntityRepository permissionEntityRepository;
    private final RolePermissionEntityMapperImpl rolePermissionEntityMapper;
    private final RolePermissionEntityRepository rolePermissionEntityRepository;
    private final RolePermissionDomainMapper rolePermissionDomainMapper;

    @Override
    public RoleResponse create(RoleCreationRequest request) {
        if (roleEntityRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        RoleCreationCmd cmd = roleDomainMapper.createFrom(request);

        List<Permission> permissions = findPermission(request.getPermissions());
        List<RolePermissionCmd> rolePermissionCmds =
                rolePermissionDomainMapper.toRolePermissionCmdList(permissions);
        cmd.setRolePermissionCmds(rolePermissionCmds);

        Role role = new Role(cmd);
        roleDomainRepository.save(role);

        return roleDomainMapper.responseFrom(role);
    }

    @Override
    public RoleResponse update(String name, RoleUpdateRequest request) {
        Role role = findRole(name);
        RoleUpdateCmd cmd = new RoleUpdateCmd();
        roleDomainMapper.updateFrom(cmd, request);

        List<Permission> permissions = findPermission(request.getPermissions());
        List<RolePermissionCmd> rolePermissionCmds =
                rolePermissionDomainMapper.toRolePermissionCmdList(permissions);
        cmd.setRolePermissionCmds(rolePermissionCmds);
        role.update(cmd);

        roleDomainRepository.save(role);

        return roleDomainMapper.responseFrom(role);
    }

    @Override
    public RoleResponse delete(String name) {
        Role role = findRole(name);
        if (role.getDeleted()) {
            throw new AppException(ErrorCode.ROLE_DELETED);
        }
        role.delete();
        roleDomainRepository.save(role);

        return roleDomainMapper.responseFrom(role);
    }

    @Override
    public RoleResponse restore(String name) {
        Role role = findRole(name);
        if (!role.getDeleted()) {
            throw new AppException(ErrorCode.ROLE_RESTORED);
        }
        role.restore();
        roleDomainRepository.save(role);

        return roleDomainMapper.responseFrom(role);
    }

    private Role findRole(String name) {
        RoleEntity roleEntity = roleEntityRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        Role role = roleEntityMapper.toDomain(roleEntity);
        role.enrichRolePermission(fetchExistingPermission(role.getId()));
        return role;
    }

    private List<RolePermission> fetchExistingPermission(UUID id) {
        return rolePermissionEntityRepository.findPermissionByRoleId(id)
                .stream()
                .map(rolePermissionEntityMapper::toDomain)
                .toList();
    }

    private List<Permission> findPermission(List<String> name) {
        List<PermissionEntity> permissionEntities = permissionEntityRepository.findAllByNames(name);
        if (permissionEntities.size() != name.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        return permissionEntities.stream()
                .map(permissionEntityMapper::toDomain)
                .toList();
    }
}