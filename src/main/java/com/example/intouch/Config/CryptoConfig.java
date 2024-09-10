package com.example.intouch.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Configuration
public class CryptoConfig {

    private KeyGenerator keyGenerator;

    @Bean
    public KeyGenerator getKeyGenerator() throws NoSuchAlgorithmException {
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator;
    }

    @Bean
    public Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES");
    }

    @Bean
    public SecretKey getSecretKey(){
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }
}
