package com.kindergarten.api.security.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class SaltUtil {
    public String encodedPassword(String salt, String password) {
        return BCrypt.hashpw(password, salt);
    }

    public String genSalt() {
        return BCrypt.gensalt();
    }
}
