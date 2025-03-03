package com.evo.iam.infrastructure.persistence.repository;

import com.evo.iam.infrastructure.persistence.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface PermissionEntityRepository extends JpaRepository<PermissionEntity, UUID> {
    @Query("select p from PermissionEntity p where p.name in :name")
    Optional<PermissionEntity> findByName(@Param("name") String name);

    @Query("select p from PermissionEntity p where p.deleted = false and p.name in :name")
    Optional<PermissionEntity> findByNameAndNotDeleted(@Param("name") String name);

    @Query("select p from PermissionEntity p where p.deleted = false and p.name in :names")
    List<PermissionEntity> findAllByNames(@Param("names") List<String> names);
}
