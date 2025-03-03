package com.evo.storage.infrastructure.persistence.entity;

import com.evo.config.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileEntity extends AuditableEntity {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "original_file_name")
    private String originalName;

    @Column(name = "hash_file_name")
    private String hashNameMd5;

    @Column(name = "path")
    private String path;

    @Column(name = "file_type")
    private String type;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "extension")
    private String extension;

    @Column(name = "owner")
    private String owner;

    @Column(name = "visibility")
    private String visibility;

    @Column(name = "deleted")
    private Boolean deleted;
}
