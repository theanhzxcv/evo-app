package com.evo.storage.domain;

import com.evo.config.AuditableDomain;
import com.evo.storage.domain.command.FileCreationCmd;
import com.evo.storage.domain.command.FileUpdateCmd;
import com.evo.util.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class FileSt extends AuditableDomain {
    private UUID id;
    private String description;
    private String originalName;
    private String hashNameMd5;
    private String path;
    private String type;
    private String contentType;
    private String extension;
    private String owner;
    private String visibility;
    private Boolean deleted;

    public FileSt(FileCreationCmd cmd) {
        this.id = IdUtils.nextId();
        this.description = cmd.getDescription();
        this.originalName = cmd.getFile().getOriginalFilename();
        this.hashNameMd5 = cmd.getHashNameMd5();
        this.path = cmd.getPath();
        this.type = cmd.getType();
        this.contentType = cmd.getFile().getContentType();
        this.extension = cmd.getExtension();
        this.owner = cmd.getOwner();
        this.visibility = cmd.getVisibility();
        this.deleted = false;
    }

    public void update(FileUpdateCmd cmd) {
        this.description = cmd.getDescription();
        this.owner = cmd.getOwner();
        this.visibility = cmd.getVisibility();
    }

    public void delete() {
        this.deleted = true;
    }

    public void undelete() {
        this.deleted = false;
    }
}
