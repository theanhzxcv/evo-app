package com.evo.iam.infrastructure.repository;

import com.evo.iam.domain.User;
import com.evo.iam.domain.repository.UserDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.UserActivityEntity;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.infrastructure.persistence.entities.UserRoleEntity;
import com.evo.iam.infrastructure.persistence.mapper.UserActivityEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.UserRoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.UserActivityEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserRoleEntityRepository;
import com.evo.support.AbstractDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserDomainRepositoryImpl
        extends AbstractDomainRepository<User, UserEntity, UUID>
        implements UserDomainRepository {

    private final UserEntityMapperImpl userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final UserRoleEntityMapperImpl userRoleEntityMapper;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final UserActivityEntityMapperImpl userActivityEntityMapper;
    private final UserActivityEntityRepository userActivityEntityRepository;

    protected UserDomainRepositoryImpl(UserEntityMapperImpl userEntityMapper,
                                       UserEntityRepository userEntityRepository,
                                       UserRoleEntityMapperImpl userRoleEntityMapper,
                                       UserRoleEntityRepository userRoleEntityRepository,
                                       UserActivityEntityMapperImpl userActivityEntityMapper,
                                       UserActivityEntityRepository userActivityEntityRepository
    ) {
        super(userEntityRepository, userEntityMapper);
        this.userEntityMapper = userEntityMapper;
        this.userRoleEntityMapper = userRoleEntityMapper;
        this.userActivityEntityMapper = userActivityEntityMapper;
        this.userEntityRepository = userEntityRepository;
        this.userRoleEntityRepository = userRoleEntityRepository;
        this.userActivityEntityRepository = userActivityEntityRepository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userEntityMapper.toEntity(user);
        this.userEntityRepository.save(userEntity);

        if (!CollectionUtils.isEmpty(user.getUserActivities())) {
            List<UserActivityEntity> userActivityEntities = user.getUserActivities().stream()
                    .map(userActivityEntityMapper::toEntity)
                    .collect(Collectors.toList());
            userActivityEntityRepository.saveAll(userActivityEntities);
        }

        if (!CollectionUtils.isEmpty(user.getUserRoles())) {
            List<UserRoleEntity> userRoleEntities = user.getUserRoles().stream()
                    .map(userRoleEntityMapper::toEntity)
                    .toList();
            userRoleEntityRepository.saveAll(userRoleEntities);
        }

        return user;
    }

    @Override
    public User getById(UUID id) {
        return null;
    }

    @Override
    public void existsById(UUID id) {}
}
