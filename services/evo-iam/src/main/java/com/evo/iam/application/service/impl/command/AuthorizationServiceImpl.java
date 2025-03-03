package com.evo.iam.application.service.impl.command;

import com.evo.UserAuthority;
import com.evo.iam.domain.*;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.infrastructure.persistence.mapper.*;
import com.evo.iam.infrastructure.persistence.repository.*;
import com.evo.security.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorityService {

    private final UserEntityMapperImpl userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final RoleEntityMapperImpl roleEntityMapper;
    private final RoleEntityRepository roleEntityRepository;
    private final UserRoleEntityMapperImpl userRoleEntityMapper;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final PermissionEntityMapperImpl permissionEntityMapper;
    private final PermissionEntityRepository permissionEntityRepository;
    private final RolePermissionEntityMapperImpl rolePermissionEntityMapper;
    private final RolePermissionEntityRepository rolePermissionEntityRepository;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        return null;
    }

    @Override
    public UserAuthority getUserAuthority(String email) {
        User user = findUser(email);

        return buildUserAuthority(user);
    }

    @Override
    public UserAuthority getClientAuthority(UUID clientId) {
        return null;
    }

    private UserAuthority buildUserAuthority(User user) {
        List<UserRole> userRoles = userRoleEntityRepository.findByUserId(user.getId()).stream()
                .map(userRoleEntityMapper::toDomain)
                .toList();

        List<UUID> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        Map<UUID, Role> roleMap = roleEntityRepository.findAllById(roleIds).stream()
                .map(roleEntityMapper::toDomain)
                .collect(Collectors.toMap(Role::getId, Function.identity()));

        boolean isRoot = roleMap.values().stream().anyMatch(Role::getRoot);

        List<RolePermission> rolePermissions = rolePermissionEntityRepository.findAllByRoleIdIn(roleIds).stream()
                .map(rolePermissionEntityMapper::toDomain)
                .toList();

        List<UUID> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        Map<UUID, Permission> permissionMap = permissionEntityRepository.findAllById(permissionIds).stream()
                .map(permissionEntityMapper::toDomain)
                .collect(Collectors.toMap(Permission::getId, Function.identity()));

        List<String> grantedPermissions = rolePermissions.stream()
                .map(rolePermission -> {
                    Permission permission = permissionMap.get(rolePermission.getPermissionId());
                    if (permission == null) {
                        throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
                    }
                    return permission.getResource() + ":" + permission.getScope();
                })
                .toList();

        return UserAuthority.builder()
                .userId(user.getId())
                .isRoot(isRoot)
                .grantedPermissions(grantedPermissions)
                .build();
    }


    private User findUser(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userEntityMapper.toDomain(userEntity);
    }
}
