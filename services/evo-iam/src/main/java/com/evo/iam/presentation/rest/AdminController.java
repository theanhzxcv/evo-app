package com.evo.iam.presentation.rest;

import com.evo.iam.application.dto.request.PermissionCreationRequest;
import com.evo.iam.application.dto.request.PermissionUpdateRequest;
import com.evo.iam.application.dto.request.RoleCreationRequest;
import com.evo.iam.application.dto.request.RoleUpdateRequest;
import com.evo.iam.application.dto.response.PermissionResponse;
import com.evo.iam.application.dto.response.RoleResponse;
import com.evo.iam.application.dto.response.api.ApiResponse;
import com.evo.iam.application.dto.response.api.PageApiResponse;
import com.evo.iam.application.service.PermissionCmdService;
import com.evo.iam.application.service.PermissionQueryService;
import com.evo.iam.application.service.RoleCmdService;
import com.evo.iam.application.service.RoleQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin")
public class AdminController {

    private final RoleCmdService roleCmdService;
    private final RoleQueryService roleQueryService;
    private final PermissionCmdService permissionCmdService;
    private final PermissionQueryService permissionQueryService;

    @PreAuthorize("hasPermission('Permission','Create')")
    @PostMapping("/permissions")
    public ApiResponse<PermissionResponse> createPermission(
            @ParameterObject @Valid PermissionCreationRequest permissionCreationRequest) {
        try {
            PermissionResponse newPermission = permissionCmdService.create(permissionCreationRequest);

            return ApiResponse.created(newPermission).success("New permission created.");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST,
                    "Failed to create new permission.");
        }
    }

    @PreAuthorize("hasPermission('Permission','Read')")
    @GetMapping("/permissions")
    public PageApiResponse<PermissionResponse> allPermission(@RequestParam int pageIndex,
                                                             @RequestParam int pageSize) {
        try {
            Page<PermissionResponse> allPermissions = permissionQueryService.allPermissions(pageIndex, pageSize);

            return PageApiResponse.of(allPermissions.getContent(),
                    allPermissions.getNumber(),
                    allPermissions.getSize(),
                    allPermissions.getTotalElements());
        } catch (Exception e) {
            return PageApiResponse.failPaging("Failed to retrieve permissions.");
        }
    }

    @PreAuthorize("hasPermission('Permission','Update')")
    @PutMapping("/permissions/{name}")
    public ApiResponse<PermissionResponse> updatePermission(
            @PathVariable("name") String name,
            @ParameterObject @Valid PermissionUpdateRequest permissionUpdateRequest) {
        try {
            PermissionResponse updatedPermission = permissionCmdService.update(name, permissionUpdateRequest);

            return ApiResponse.of(updatedPermission).success("Permission updated.");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to update permission.");
        }
    }

    @PreAuthorize("hasPermission('Permission','Delete')")
    @DeleteMapping("/permissions/{name}")
    public ApiResponse<PermissionResponse> deletePermission(@PathVariable("name") String name) {
        try {
            PermissionResponse deletedPermission = permissionCmdService.delete(name);

            return ApiResponse.of(deletedPermission).success("Permission deleted.");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to delete permission.");
        }
    }

    @PreAuthorize("hasPermission('Permission','Update')")
    @PutMapping("/permissions/{name}/restore")
    public ApiResponse<PermissionResponse> restorePermission(@PathVariable("name") String name) {
        try {
            PermissionResponse restoredPermission = permissionCmdService.restore(name);

            return ApiResponse.of(restoredPermission).success("Permission restored.");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to restore permission.");
        }
    }

    @PreAuthorize("hasPermission('Role','Create')")
    @PostMapping("/roles")
    public ApiResponse<RoleResponse> createRole(
            @ParameterObject @Valid RoleCreationRequest roleCreationRequest) {
        try {
            RoleResponse newRole = roleCmdService.create(roleCreationRequest);

            return ApiResponse.created(newRole).success("New role created");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to create new role.");
        }
    }

    @PreAuthorize("hasPermission('Role','Read')")
    @GetMapping("/roles")
    public PageApiResponse<RoleResponse> allRoles(@RequestParam int pageIndex,
                                                    @RequestParam int pageSize) {
        try {
            Page<RoleResponse> allRoles = roleQueryService.allRoles(pageIndex, pageSize);

            return PageApiResponse.of(allRoles.getContent(),
                            allRoles.getNumber(),
                            allRoles.getSize(),
                            allRoles.getTotalElements());
        } catch (Exception e) {
            return PageApiResponse.failPaging("Failed to retrieve roles.");
        }
    }

    @PreAuthorize("hasPermission('Role','Update')")
    @PutMapping("/roles/{name}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable("name") String name,
            @ParameterObject @Valid RoleUpdateRequest roleUpdateRequest) {
        try {
            RoleResponse updatedRole = roleCmdService.update(name, roleUpdateRequest);

            return ApiResponse.of(updatedRole).success("Role updated");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to update role.");
        }
    }

    @PreAuthorize("hasPermission('Role','Delete')")
    @DeleteMapping("/roles/{name}")
    public ApiResponse<RoleResponse> deleteRole(@PathVariable("name") String name) {
        try {
            RoleResponse deletedRole = roleCmdService.delete(name);

            return ApiResponse.of(deletedRole).success("Role deleted");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to delete role.");
        }
    }

    @PreAuthorize("hasPermission('Role','Update')")
    @PutMapping("/roles/{name}/restore")
    public ApiResponse<RoleResponse> restoreRole(@PathVariable("name") String name) {
        try {
            RoleResponse restoredRole = roleCmdService.restore(name);

            return ApiResponse.of(restoredRole).success("Role restored");
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.BAD_REQUEST, "Failed to restore role.");
        }
    }
}
