package com.evo.iam.infrastructure.persistence.repository;


import com.evo.iam.infrastructure.persistence.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, Long> {
    List<UserRoleEntity> findByUserId(UUID id);

    List<UserRoleEntity> findRoleByUserId(UUID id);
}
