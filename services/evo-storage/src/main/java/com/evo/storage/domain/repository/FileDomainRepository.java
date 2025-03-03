package com.evo.storage.domain.repository;

import com.evo.storage.domain.FileSt;
import com.evo.support.DomainRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface FileDomainRepository extends DomainRepository<FileSt, UUID> {}
