package com.evo.iam.infrastructure.persistence.repository;

import com.evo.iam.infrastructure.persistence.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleEntityRepository extends JpaRepository<RoleEntity, UUID> {

    @Query("select r from RoleEntity r where r.name = :name")
    Optional<RoleEntity> findByName(@Param("name") String name);

    @Query("select r from RoleEntity r where r.name in :names")
    List<RoleEntity> findAllByNames(@Param("names") List<String> names);
}
