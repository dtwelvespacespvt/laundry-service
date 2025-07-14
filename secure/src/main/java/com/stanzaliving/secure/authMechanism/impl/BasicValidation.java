package com.stanzaliving.secure.authMechanism.impl;


import com.stanzaliving.secure.authMechanism.AuthStrategy;
import com.stanzaliving.secure.dto.ValidateTokenRequestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BasicValidation implements AuthStrategy {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createSecret(String text) {
        String secretToken = passwordEncoder.encode(text);
        return secretToken;
    }

    public boolean validateToken(ValidateTokenRequestDto validateTokenRequestDto) {
        if (!passwordEncoder.matches(validateTokenRequestDto.getUserId(), validateTokenRequestDto.getSecretToken())) {
            log.error("Invalid Token , user{}", validateTokenRequestDto.getUserId());
            return false;
        }
        return true;
    }
}
