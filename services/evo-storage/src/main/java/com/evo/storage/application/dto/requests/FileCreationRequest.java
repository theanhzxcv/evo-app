package com.evo.storage.application.dto.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileCreationRequest {
    private MultipartFile file;
    private String description;
}
