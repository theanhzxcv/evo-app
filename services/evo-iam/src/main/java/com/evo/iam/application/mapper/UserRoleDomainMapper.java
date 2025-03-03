package com.evo.iam.application.mapper;

import com.evo.iam.domain.Role;
import com.evo.iam.domain.command.UserRoleCmd;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRoleDomainMapper {

    public UserRoleCmd toUserRoleCmd(Role role) {
        return UserRoleCmd.builder()
                .roleId(role.getId())
                .build();
    }

    public List<UserRoleCmd> toUserRoleCmdList(List<Role> roles) {
        return roles.stream()
                .map(this::toUserRoleCmd)
                .collect(Collectors.toList());
    }
}
