package com.evo.iam.application.mapper;

import com.evo.iam.domain.command.UserActivityCmd;
import org.springframework.stereotype.Component;

@Component
public class UserActivityDomainMapper {
    public UserActivityCmd toUserActivityCmd(String email, String activity) {
        return UserActivityCmd.builder()
                .email(email)
                .activity(activity)
                .build();
    }
}
