package com.evo.iam.infrastructure.persistence.mapper;

import com.evo.iam.domain.User;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.support.DomainEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserEntityMapperImpl implements DomainEntityMapper<User, UserEntity> {

    @Override
    public User toDomain(UserEntity entity) {
        return Optional.ofNullable(entity)
                .map(userEntity -> User.builder()
                        .id(userEntity.getId())
                        .avatarId(userEntity.getAvatarId())
                        .username(userEntity.getUsername())
                        .email(userEntity.getEmail())
                        .password(userEntity.getPassword())
                        .firstName(userEntity.getFirstName())
                        .lastName(userEntity.getLastName())
                        .address(userEntity.getAddress())
                        .phone(userEntity.getPhone())
                        .dateOfBirth(userEntity.getDateOfBirth())
                        .status(userEntity.getStatus())
                        .tfaEnabled(userEntity.getTfaEnabled())
                        .secretKey(userEntity.getSecretKey())
                        .verified(userEntity.getVerified())
                        .deleted(userEntity.getDeleted())
                        .createdBy(userEntity.getCreatedBy())
                        .createdAt(userEntity.getCreatedAt())
                        .lastModifiedBy(userEntity.getLastModifiedBy())
                        .lastModifiedAt(userEntity.getLastModifiedAt())
                        .build())
                .orElse(null);
    }

    @Override
    public UserEntity toEntity(User domain) {
        return Optional.ofNullable(domain)
                .map(userDomain -> UserEntity.builder()
                        .id(userDomain.getId())
                        .avatarId(userDomain.getAvatarId())
                        .username(userDomain.getUsername())
                        .email(userDomain.getEmail())
                        .password(userDomain.getPassword())
                        .firstName(userDomain.getFirstName())
                        .lastName(userDomain.getLastName())
                        .address(userDomain.getAddress())
                        .phone(userDomain.getPhone())
                        .dateOfBirth(userDomain.getDateOfBirth())
                        .status(userDomain.getStatus())
                        .tfaEnabled(userDomain.getTfaEnabled())
                        .secretKey(userDomain.getSecretKey())
                        .verified(userDomain.getVerified())
                        .deleted(userDomain.getDeleted())
                        .createdBy(userDomain.getCreatedBy())
                        .createdAt(userDomain.getCreatedAt())
                        .lastModifiedBy(userDomain.getLastModifiedBy())
                        .lastModifiedAt(userDomain.getLastModifiedAt())
                        .build())
                .orElse(null);
    }
}
