package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.UserSearchRequest;
import com.evo.iam.application.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserQueryService {

    Page<UserResponse> searchUser(UserSearchRequest request);

    Page<UserResponse> allUsers(int pageIndex, int pageSize);
}
