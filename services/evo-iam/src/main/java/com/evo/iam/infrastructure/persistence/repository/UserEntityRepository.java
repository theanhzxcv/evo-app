package com.evo.iam.infrastructure.persistence.repository;

import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select u from UserEntity u where u.deleted = false and u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    Optional<UserEntity> findByUsername(String username);
}
