package com.evo.iam.infrastructure.persistence.mapper;

import com.evo.iam.domain.UserActivity;
import com.evo.iam.infrastructure.persistence.entities.UserActivityEntity;
import com.evo.support.DomainEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class UserActivityEntityMapperImpl implements DomainEntityMapper<UserActivity, UserActivityEntity> {

    @Override
    public UserActivity toDomain(UserActivityEntity userActivityEntity) {
        if (userActivityEntity == null) {
            return null;
        }
        return UserActivity.builder()
                .id(userActivityEntity.getId())
                .ip(userActivityEntity.getIp())
                .email(userActivityEntity.getEmail())
                .activity(userActivityEntity.getActivity())
                .logAt(userActivityEntity.getLogAt())
                .build();
    }

    @Override
    public UserActivityEntity toEntity(UserActivity userActivityDomain) {
        if (userActivityDomain == null) {
            return null;
        }
        return UserActivityEntity.builder()
                .id(userActivityDomain.getId())
                .ip(userActivityDomain.getIp())
                .email(userActivityDomain.getEmail())
                .activity(userActivityDomain.getActivity())
                .logAt(userActivityDomain.getLogAt())
                .build();
    }
}
