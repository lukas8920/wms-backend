package org.kehrbusch.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        String input = "test";
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encoded = encoder.encode(input);
        System.out.println(encoded);
    }
}
