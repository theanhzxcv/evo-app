package com.evo.iam.application;

import com.evo.iam.application.exception.AppException;
import com.evo.iam.application.exception.ErrorCode;
import com.evo.iam.application.mapper.UserDomainMapper;
import com.evo.iam.application.mapper.UserRoleDomainMapper;
import com.evo.iam.domain.Role;
import com.evo.iam.domain.User;
import com.evo.iam.domain.command.UserCreationCmd;
import com.evo.iam.domain.command.UserRoleCmd;
import com.evo.iam.domain.repository.RoleDomainRepository;
import com.evo.iam.domain.repository.UserDomainRepository;
import com.evo.iam.infrastructure.adapter.keycloak.KeycloakUtils;
import com.evo.iam.infrastructure.adapter.tfa.TfaService;
import com.evo.iam.infrastructure.persistence.mapper.RoleEntityMapperImpl;
import com.evo.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.evo.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.util.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final KeycloakUtils keycloakUtils;
    private final TfaService tfaService;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;
    private final UserDomainRepository userDomainRepository;
    private final UserDomainMapper userDomainMapper;
    private final RoleEntityRepository roleEntityRepository;
    private final RoleDomainRepository roleDomainRepository;
    private final RoleEntityMapperImpl roleEntityMapper;
    private final UserRoleDomainMapper userRoleDomainMapper;

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedAdminAccount();
    }

    private void seedAdminAccount() {
        String adminEmail = "anhnt@evotek.vn";
        String password = "anhnt@123";

        if (userEntityRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        Role role = roleEntityRepository.findByName("ADMIN")
                .map(roleEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR));

        UserCreationCmd creationCmd = userDomainMapper.createFrom(adminEmail, password);
        UserRoleCmd userRoleCmd = userRoleDomainMapper.toUserRoleCmd(role);
        List<UserRoleCmd> userRoleCmds = new ArrayList<>();
        userRoleCmds.add(userRoleCmd);
        creationCmd.setSecretKey(tfaService.generateSecretKey());
        creationCmd.setUserRoleCmds(userRoleCmds);
        keycloakUtils.createKeycloakUser(creationCmd);
        creationCmd.setPassword(passwordEncoder.encode(password));
        User adminUser = new User(creationCmd);
        adminUser.markAsActivated();

        userDomainRepository.save(adminUser);
    }

    private void seedRoles() {
        String roleName = "ADMIN";

        if (roleEntityRepository.findByName(roleName).isEmpty()) {
            Role role = Role.builder()
                    .id(IdUtils.nextId())
                    .name(roleName)
                    .root(true)
                    .deleted(false)
                    .build();

            roleDomainRepository.save(role);
        }
    }


}
