package com.evo.iam.infrastructure.persistence.repository;

import com.evo.iam.domain.User;
import com.evo.iam.domain.query.UserSearchQuery;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;

import java.util.List;

public interface UserEntityRepositoryCustom {

    List<UserEntity> search(UserSearchQuery userSearchQuery);

    Long count(UserSearchQuery userSearchQuery);
}
