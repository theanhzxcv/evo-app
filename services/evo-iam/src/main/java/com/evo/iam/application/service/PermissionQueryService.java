package com.evo.iam.application.service;

import com.evo.iam.application.dto.response.PermissionResponse;
import org.springframework.data.domain.Page;

public interface PermissionQueryService {

    Page<PermissionResponse> allPermissions(int pageIndex, int pageSize);
}
