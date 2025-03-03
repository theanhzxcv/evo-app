package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.UserCreationRequest;
import com.evo.iam.application.dto.request.UserUpdateRequest;
import com.evo.iam.application.dto.request.PasswordChangeRequest;
import com.evo.iam.application.dto.response.UserResponse;

public interface UserCmdService {

    UserResponse create(UserCreationRequest request);

    UserResponse update(String email, UserUpdateRequest request);

    UserResponse delete(String email);

    UserResponse restore(String email);

    String changeSystemPassword(String email, PasswordChangeRequest request);

    String changeKeyCloakPassword(String email, PasswordChangeRequest request);
}
