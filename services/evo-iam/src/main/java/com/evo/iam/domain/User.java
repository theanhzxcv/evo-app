package com.evo.iam.domain;

import com.evo.config.AuditableDomain;
import com.evo.iam.domain.command.*;
import com.evo.util.IdUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuperBuilder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditableDomain {
    private UUID id;
    private UUID avatarId;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String status;
    private LocalDate dateOfBirth;
    private String secretKey;
    private Boolean tfaEnabled;
    private Boolean verified;
    private Boolean deleted;
    List<UserRole> userRoles;
    List<UserActivity> userActivities;

    public User(UserCreationCmd cmd) {
        this.id = IdUtils.nextId();
        this.username = cmd.getUsername();
        this.email= cmd.getEmail();
        this.password = cmd.getPassword();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.tfaEnabled = cmd.getTfaEnabled();
        this.secretKey = cmd.getSecretKey();
        this.verified = cmd.getVerified();
        this.deleted = false;
        this.status = "deactivated";
        this.updateUserRole(cmd.getUserRoleCmds());
    }

    public void update(UserUpdateCmd cmd) {
        this.username = cmd.getUsername();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.address = cmd.getAddress();
        this.phone = cmd.getPhone();
        this.dateOfBirth = cmd.getDateOfBirth();
        this.status = "activated";
        this.updateUserRole(cmd.getUserRoleCmds());
    }

    public void updateUserRole(List<UserRoleCmd> cmds) {
        if (CollectionUtils.isEmpty(this.userRoles)) {
            this.userRoles = new ArrayList<>();
        }

        Map<UUID, UserRole> existingRoleMap = this.userRoles.stream()
                .collect(Collectors.toMap(UserRole::getRoleId, userRole -> {
                    userRole.delete();
                    return userRole;
                }));

        cmds.stream()
                .filter(cmd -> existingRoleMap.containsKey(cmd.getRoleId()))
                .forEach(cmd -> existingRoleMap.get(cmd.getRoleId()).undelete());

        cmds.stream()
                .filter(cmd -> !existingRoleMap.containsKey(cmd.getRoleId()))
                .map(cmd -> UserRole.builder()
                        .id(IdUtils.nextId())
                        .userId(this.id)
                        .roleId(cmd.getRoleId())
                        .deleted(false)
                        .build())
                .forEach(this.userRoles::add);
    }

    public User(UserRegistrationCmd cmd) {
        this.id = IdUtils.nextId();
        this.username = cmd.getUsername();
        this.email= cmd.getEmail();
        this.password = cmd.getPassword();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.tfaEnabled = cmd.getTfaEnabled();
        this.secretKey = cmd.getSecretKey();
        this.verified = cmd.getVerified();
        this.deleted = false;
        this.status = "deactivated";
        if (cmd.getUserRoleCmd() != null) {
            this.userRoles = List.of(UserRole.builder()
                            .id(IdUtils.nextId())
                            .userId(this.id)
                            .roleId(cmd.getUserRoleCmd().getRoleId())
                            .deleted(false).build());
        } else {
            this.userRoles = new ArrayList<>();
        }
    }

    public void updateProfile(UserUpdateCmd cmd) {
        this.username = cmd.getUsername();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.address = cmd.getAddress();
        this.phone = cmd.getPhone();
        this.dateOfBirth = cmd.getDateOfBirth();
        this.status = "activated";
    }

    public void uploadAvatar(UploadAvatarCmd cmd) {
        this.avatarId = cmd.getAvatarId();
    }

    public void activate(ActivateAccountCmd cmd) {
        this.address = cmd.getAddress();
        this.phone = cmd.getPhone();
        this.dateOfBirth = cmd.getDateOfBirth();
        this.status = "activated";
    }

    public void markAsActivated() {
        this.status = "activated";
    }

    public void markAsDeactivated() {
        this.status = "deactivated";
    }

    public void markAsVerified() {
        this.verified = true;
    }

    public void markAsUnverified() {
        this.verified = false;
    }

    public void logActivity(UserActivityCmd cmd, HttpServletRequest request) {
        this.userActivities = new ArrayList<>();
        this.userActivities.add(new UserActivity(cmd, request));
    }

    public void delete() {
        this.deleted = true;
        if (!CollectionUtils.isEmpty(this.userRoles)) {
            this.userRoles.forEach(UserRole::delete);
        }
    }

    public void restore() {
        this.deleted = false;
        if (!CollectionUtils.isEmpty(this.userRoles)) {
            this.userRoles.forEach(UserRole::undelete);
        }
    }

    public void changePassword(PasswordChangeCmd cmd) {
        this.password = cmd.getPassword();
    }

    public void enrichUserRole(List<UserRole> userRoles) {
        if (CollectionUtils.isEmpty(this.userRoles)) {
            this.userRoles = new ArrayList<>();
        }

        this.userRoles.addAll(userRoles);
    }
}
