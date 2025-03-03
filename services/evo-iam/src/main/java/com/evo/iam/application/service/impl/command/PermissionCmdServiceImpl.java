package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.PermissionCreationRequest;
import com.evo.iam.application.dto.request.PermissionUpdateRequest;
import com.evo.iam.application.dto.response.PermissionResponse;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.PermissionDomainMapper;
import com.evo.iam.application.service.PermissionCmdService;
import com.evo.iam.domain.Permission;
import com.evo.iam.domain.command.PermissionCreationCmd;
import com.evo.iam.domain.command.PermissionUpdateCmd;
import com.evo.iam.domain.repository.PermissionDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.PermissionEntity;
import com.evo.iam.infrastructure.persistence.mapper.PermissionEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionCmdServiceImpl implements PermissionCmdService {

    private final PermissionDomainMapper permissionDomainMapper;
    private final PermissionEntityMapperImpl permissionEntityMapper;
    private final PermissionEntityRepository permissionEntityRepository;
    private final PermissionDomainRepository permissionDomainRepository;

    @Override
    @Transactional
    public PermissionResponse create(PermissionCreationRequest request) {
        if (permissionEntityRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
        }
        PermissionCreationCmd cmd = permissionDomainMapper.createFrom(request);
        Permission permission = new Permission(cmd);
        permissionDomainRepository.save(permission);

        return permissionDomainMapper.responseFrom(permission);
    }

    @Override
    @Transactional
    public PermissionResponse update(String name, PermissionUpdateRequest request) {
        Permission permission = findPermission(name);
        PermissionUpdateCmd cmd = new PermissionUpdateCmd();
        permissionDomainMapper.updateFrom(request, cmd);
        permission.update(cmd);
        permissionDomainRepository.save(permission);

        return permissionDomainMapper.responseFrom(permission);
    }

    @Override
    @Transactional
    public PermissionResponse delete(String name) {
        Permission permission = permissionEntityRepository.findByName(name)
                .map(permissionEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        if (permission.getDeleted()) {
            throw new AppException(ErrorCode.PERMISSION_DELETED);
        }
        permission.delete();
        permissionDomainRepository.save(permission);

        return permissionDomainMapper.responseFrom(permission);
    }

    @Override
    @Transactional
    public PermissionResponse restore(String name) {
        Permission permission = permissionEntityRepository.findByName(name)
                .map(permissionEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        if (!permission.getDeleted()) {
            throw new AppException(ErrorCode.PERMISSION_RESTORED);
        }
        permission.undelete();
        permissionDomainRepository.save(permission);

        return permissionDomainMapper.responseFrom(permission);
    }

    private Permission findPermission(String name) {
        return permissionEntityRepository.findByNameAndNotDeleted(name)
                .map(permissionEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
    }
}
