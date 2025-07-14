package com.stanzaliving.secure.authMechanism;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Configuration
@Log4j2
public class SecurityConfig {

    @Value("${aes.secret.key}")
    private String aesSecretKey;
    @Bean
    public PasswordEncoder bcrptBCryptPasswordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    public static final String ALGORITHM = "AES";
    @Bean
    public SecretKeySpec prepareSecreteKey() throws Exception {
        MessageDigest sha = null;
        byte[] key;
        SecretKeySpec secretKey ;
        try {
            key = aesSecretKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
            return secretKey ;
        } catch (NoSuchAlgorithmException e) {
            log.error("Error while prepareing secret key ");
            throw new Exception("Error while prepearing key");
        }
    }
}
