package com.evo.iam.application.service;

import com.evo.iam.application.dto.response.RoleResponse;
import org.springframework.data.domain.Page;

public interface RoleQueryService {

    Page<RoleResponse> allRoles(int pageIndex, int pageSize);
}
