package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.RoleCreationRequest;
import com.evo.iam.application.dto.request.RoleUpdateRequest;
import com.evo.iam.application.dto.response.RoleResponse;

public interface RoleCmdService {

    RoleResponse create(RoleCreationRequest request);

    RoleResponse update(String name, RoleUpdateRequest request);

    RoleResponse delete(String name);

    RoleResponse restore(String name);
}
