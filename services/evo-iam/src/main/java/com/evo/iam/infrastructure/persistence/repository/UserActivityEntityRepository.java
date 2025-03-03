package com.evo.iam.infrastructure.persistence.repository;

import com.evo.iam.infrastructure.persistence.entities.UserActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserActivityEntityRepository extends JpaRepository<UserActivityEntity, UUID> {
}
