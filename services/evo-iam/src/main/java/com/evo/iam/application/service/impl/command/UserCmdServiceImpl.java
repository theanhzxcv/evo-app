package com.evo.iam.application.service.impl.command;

import com.evo.iam.application.dto.request.UserCreationRequest;
import com.evo.iam.application.dto.request.UserUpdateRequest;
import com.evo.iam.application.dto.request.PasswordChangeRequest;
import com.evo.iam.application.dto.response.UserResponse;
import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.UserActivityDomainMapper;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.application.mapper.UserRoleDomainMapper;
import com.evo.iam.infrastructure.adapter.tfa.impl.TfaServiceImpl;
import com.evo.iam.domain.*;
import com.evo.iam.domain.command.*;
import com.evo.iam.infrastructure.adapter.keycloak.KeycloakUtils;
import com.evo.iam.application.service.UserCmdService;
import com.evo.iam.domain.repository.UserDomainRepository;
import com.evo.iam.infrastructure.persistence.entities.RoleEntity;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import com.evo.iam.infrastructure.persistence.mapper.RoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.UserEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.mapper.UserRoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserRoleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCmdServiceImpl implements UserCmdService {

    private final TfaServiceImpl tfaService;
    private final KeycloakUtils keycloakUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapperImpl userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final UserDomainMapper userDomainMapper;
    private final UserDomainRepository userDomainRepository;
    private final RoleEntityMapperImpl roleEntityMapper;
    private final RoleEntityRepository roleEntityRepository;
    private final UserRoleDomainMapper userRoleDomainMapper;
    private final UserRoleEntityMapperImpl userRoleEntityMapper;
    private final UserRoleEntityRepository userRoleEntityRepository;

    @Override
    public UserResponse create(UserCreationRequest request) {
        if (userEntityRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserCreationCmd cmd = userDomainMapper.createFrom(request);

        List<Role> roles = findRole(request.getRoles());
        List<UserRoleCmd> userRoleCmds = userRoleDomainMapper.toUserRoleCmdList(roles);
        cmd.setUserRoleCmds(userRoleCmds);
        cmd.setSecretKey(tfaService.generateSecretKey());
        keycloakUtils.createKeycloakUser(cmd);
        cmd.setPassword(passwordEncoder.encode(request.getPassword()));

        User user = new User(cmd);
        userDomainRepository.save(user);

        return userDomainMapper.responseFrom(user);
    }

    @Override
    public UserResponse update(String email, UserUpdateRequest request) {
        User user = findUser(email);
        UserUpdateCmd cmd = new UserUpdateCmd();
        userDomainMapper.updateFrom(cmd, request);

        List<Role> roles = findRole(request.getRoles());
        List<UserRoleCmd> userRoleCmds =
                userRoleDomainMapper.toUserRoleCmdList(roles);
        cmd.setUserRoleCmds(userRoleCmds);
        user.update(cmd);

        userDomainRepository.save(user);
        keycloakUtils.updateKeycloakUser(email, cmd);

        return userDomainMapper.responseFrom(user);
    }

    @Override
    public UserResponse delete(String email) {
        User user = findUser(email);
        if (user.getDeleted()) {
            throw new AppException(ErrorCode.USER_DELETED);
        }
        user.delete();
        userDomainRepository.save(user);

        return userDomainMapper.responseFrom(user);
    }

    @Override
    public UserResponse restore(String email) {
        User user = findUser(email);
        if (!user.getDeleted()) {
            throw new AppException(ErrorCode.USER_RESTORED);
        }
        user.restore();
        userDomainRepository.save(user);

        return userDomainMapper.responseFrom(user);
    }

    @Override
    public String changeSystemPassword(String email, PasswordChangeRequest request) {
        User user = findUser(email);

        if (!request.getConfirmPassword().equals(request.getNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        PasswordChangeCmd cmd = new PasswordChangeCmd();
        userDomainMapper.changePasswordFrom(cmd, request);
        cmd.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.changePassword(cmd);

        userDomainRepository.save(user);

        return "Password changed successfully.";
    }

    @Override
    public String changeKeyCloakPassword(String email, PasswordChangeRequest request) {
        if (!request.getConfirmPassword().equals(request.getNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        PasswordChangeCmd cmd = new PasswordChangeCmd();
        userDomainMapper.changePasswordFrom(cmd, request);
        keycloakUtils.changeKeycloakPassword(cmd);

        return "Password changed successfully.";
    }

    private User findUser(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User user = userEntityMapper.toDomain(userEntity);
        user.enrichUserRole(fetchExistingRole(user.getId()));

        return user;
    }

    private List<UserRole> fetchExistingRole(UUID id) {
        return userRoleEntityRepository.findRoleByUserId(id)
                .stream()
                .map(userRoleEntityMapper::toDomain)
                .toList();
    }

    private List<Role> findRole(List<String> name) {
        List<RoleEntity> roleEntities = roleEntityRepository.findAllByNames(name);
        if (roleEntities.size() != name.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        return roleEntities.stream()
                .map(roleEntityMapper::toDomain)
                .toList();
    }
}
