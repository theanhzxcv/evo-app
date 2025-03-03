package com.evo.iam.application.service.impl.query;

import com.evo.iam.application.dto.response.RoleResponse;
import com.evo.iam.application.mapper.RoleDomainMapper;
import com.evo.iam.application.service.RoleQueryService;
import com.evo.iam.infrastructure.persistence.entities.RoleEntity;
import com.evo.iam.infrastructure.persistence.mapper.RoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.RoleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleQueryServiceImpl implements RoleQueryService {

    private final RoleDomainMapper roleDomainMapper;
    private final RoleEntityMapperImpl roleEntityMapper;
    private final RoleEntityRepository roleEntityRepository;

    @Override
    public Page<RoleResponse> allRoles(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex-1, pageSize);
        Page<RoleEntity> roleEntities = roleEntityRepository.findAll(pageable);

        return roleEntities.map(roleEntity ->
                roleDomainMapper.responseFrom(roleEntityMapper.toDomain(roleEntity)));
    }
}
