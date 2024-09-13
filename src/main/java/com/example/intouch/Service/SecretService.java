package com.example.intouch.Service;

import com.example.intouch.Entity.Secret;
import com.example.intouch.Repository.SecretRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecretService {

    private final SecretRepository secretRepository;
    private final SecretKey secretKey;

    public void addSecret(){
        List<Secret> list = secretRepository.findAll();
        if (list.size() == 0){
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            Secret secret = new Secret();
            secret.setKey(encodedKey);
            secretRepository.save(secret);
        }
    }

    public SecretKey getSecretKey(){
        List<Secret> list = secretRepository.findAll();
        byte decodeKey[] = Base64.getDecoder().decode(list.get(0).getKey());
        SecretKey decodeSecretKey = new SecretKeySpec(decodeKey, 0, decodeKey.length, "AES");
        return decodeSecretKey;
    }
}
