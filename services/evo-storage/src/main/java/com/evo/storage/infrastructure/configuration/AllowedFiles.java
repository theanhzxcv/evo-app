package com.evo.storage.infrastructure.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class AllowedFiles {
    @Value("${file.allowed-extensions.images}")
    private List<String> ALLOWED_IMAGES_EXTENSIONS;
    @Value("${file.allowed-extensions.documents}")
    private List<String> ALLOWED_DOCUMENTS_EXTENSIONS;
    @Value("${file.allowed-extensions.codes}")
    private List<String> ALLOWED_CODES_EXTENSIONS;
    @Value("${file.allowed-extensions.archives}")
    private List<String> ALLOWED_ARCHIVES_EXTENSIONS;

    @Value("${file.allowed-content-types}")
    private List<String> ALLOWED_CONTENT_TYPES;
}
