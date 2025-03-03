package com.evo.iam.infrastructure.adapter.tfa.impl;

import com.evo.iam.infrastructure.adapter.tfa.TfaService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class TfaServiceImpl implements TfaService {
    @Override
    public String generateSecretKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    @Override
    public boolean verifyCode(String secretKey, int verificationCode) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secretKey, verificationCode);
    }
}
