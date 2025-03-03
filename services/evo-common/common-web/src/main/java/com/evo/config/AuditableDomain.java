package com.evo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditableDomain implements Serializable {
    protected String createdBy;
    protected Instant createdAt;
    protected String lastModifiedBy;
    protected Instant lastModifiedAt;
}
