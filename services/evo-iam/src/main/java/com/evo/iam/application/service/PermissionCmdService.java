package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.PermissionCreationRequest;
import com.evo.iam.application.dto.request.PermissionUpdateRequest;
import com.evo.iam.application.dto.response.PermissionResponse;

public interface PermissionCmdService {

    PermissionResponse create(PermissionCreationRequest request);

    PermissionResponse update(String name, PermissionUpdateRequest request);

    PermissionResponse delete(String name);

    PermissionResponse restore(String name);
}
