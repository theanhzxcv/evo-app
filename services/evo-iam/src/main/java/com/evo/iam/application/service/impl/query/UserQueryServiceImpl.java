package com.evo.iam.application.service.impl.query;

import com.evo.iam.application.dto.request.UserSearchRequest;
import com.evo.iam.application.dto.response.UserResponse;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.application.service.UserQueryService;
import com.evo.iam.domain.User;
import com.evo.iam.domain.query.UserSearchQuery;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserDomainMapper userDomainMapper;
    private final UserEntityMapperImpl userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final UserEntityRepositoryCustom userEntityRepositoryCustom;

    @Override
    public Page<UserResponse> searchUser(UserSearchRequest request) {
        UserSearchQuery query = userDomainMapper.searchFrom(request);
        long total = userEntityRepositoryCustom.count(query);
        if (total == 0) {
            return Page.empty();
        }

        List<UserResponse> userResponses = userEntityRepositoryCustom.search(query).stream()
                .map(userEntityMapper::toDomain)
                .map(userDomainMapper::responseFrom)
                .toList();

        return new PageImpl<>(userResponses, PageRequest.of(query.getPageIndex(), query.getPageSize()), total);
    }

    @Override
    public Page<UserResponse> allUsers(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex-1, pageSize);
        Page<UserEntity> userEntities = userEntityRepository.findAll(pageable);

        return userEntities.map(userEntity ->
                userDomainMapper.responseFrom(userEntityMapper.toDomain(userEntity)));
    }
}
