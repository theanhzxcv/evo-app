package com.evo.iam.infrastructure.adapter.tfa;

public interface TfaService {
    String generateSecretKey();

    boolean verifyCode(String secretKey, int verificationCode);
}
