package com.evo.iam.application.service.impl.query;

import com.evo.iam.application.dto.response.PermissionResponse;
import com.evo.iam.application.mapper.PermissionDomainMapper;
import com.evo.iam.application.service.PermissionQueryService;
import com.evo.iam.infrastructure.persistence.entities.PermissionEntity;
import com.evo.iam.infrastructure.persistence.mapper.PermissionEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionQueryServiceImpl implements PermissionQueryService {

    private final PermissionDomainMapper permissionDomainMapper;
    private final PermissionEntityMapperImpl permissionEntityMapper;
    private final PermissionEntityRepository permissionEntityRepository;

    @Override
    public Page<PermissionResponse> allPermissions(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex-1, pageSize);
        Page<PermissionEntity> permissions = permissionEntityRepository.findAll(pageable);

        return permissions.map(permissionEntity ->
                permissionDomainMapper.responseFrom(permissionEntityMapper.toDomain(permissionEntity))
        );
    }
}
