package com.evo.storage.infrastructure.persistence.repository;

import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, UUID> {
    @Query("select f from FileEntity f where f.originalName in :name")
    Optional<FileEntity> findByOriginalName(@Param("name") String name);

    @Query("select f from FileEntity f where f.originalName in :name and f.deleted = false")
    Optional<FileEntity> findByOriginalNameAndNotDeleted(@Param("name") String name);


}
