package com.evo.storage.domain.command;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileCreationCmd {
    private UUID id;
    private MultipartFile file;
    private String hashNameMd5;
    private String path;
    private String type;
    private String contentType;
    private String extension;
    private String owner;
    private String description;
    private String visibility;
    private boolean deleted;
}
