package com.evo.iam.domain.repository;

import com.evo.iam.domain.User;
import com.evo.support.DomainRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDomainRepository extends DomainRepository<User, UUID> {}
