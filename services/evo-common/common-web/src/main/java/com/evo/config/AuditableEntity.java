package com.evo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity implements Serializable {
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected String createdBy;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected Instant createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    protected String lastModifiedBy;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "last_modified_at")
    protected Instant lastModifiedAt;
}
