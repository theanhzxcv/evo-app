package com.evo.iam.application.service;

import com.evo.iam.application.dto.request.PasswordChangeRequest;

public interface PasswordCmdService {
    String changePassword(PasswordChangeRequest request);
}
