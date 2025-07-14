package com.stanzaliving.secure.authMechanism.impl;


import com.stanzaliving.secure.authMechanism.AuthStrategy;
import com.stanzaliving.secure.dto.ValidateTokenRequestDto;
import com.stanzaliving.secure.error.UnauthorisedException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@Log4j2
public class AesValidation implements AuthStrategy {

    @Value("${aes.secret.key}")
    private String aesSecretKey;

    @Autowired
    private SecretKeySpec secretKey;

    private static final String ALGORITHM = "AES";

    @Override
    public boolean validateToken(ValidateTokenRequestDto validateTokenRequestDto) {
        try {
            String decryptedString = decrypt(validateTokenRequestDto.getSecretToken());
            List<String> decryptedList = Arrays.asList(decryptedString.split(":"));
            if (!validateTokenRequestDto.getUserId().equals(decryptedList.get(0))) {
                log.error("Invalid Token , user{}", validateTokenRequestDto.getUserId());
                return false;
            }
            return true;
        }catch (Exception ex){
            log.error("Error while Validating token: {} , for user : {}", ex, validateTokenRequestDto.getUserId());
        }
        return false;
    }

    public String decrypt(String strToDecrypt) throws UnauthorisedException {
        log.info("Inside encrypt :: strToDecrypt {}", strToDecrypt);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.error("Error while decrypting: " + e.toString());
            throw new UnauthorisedException("Invalid token");
        }
    }

    public String encrypt(String strToEncrypt) throws Exception {
        log.info("Inside encrypt :: strTpEncryt {}", strToEncrypt);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            log.error("Error while encrypting: " + e.toString());
            throw new Exception("Error while encrypting msg ");
        }
    }

    public void  checkAuth(HttpHeaders httpHeaders) throws Exception {
        if(httpHeaders.containsKey("auth")  && StringUtils.isNotEmpty(String.valueOf(httpHeaders.get("auth").get(0)))){
            validateToken(String.valueOf(httpHeaders.get("auth").get(0))) ;
        }else{
            throw new UnauthorisedException("Invalid Header " + httpHeaders) ;
        }
    }

    public boolean validateToken(String encodedString) throws UnauthorisedException {
        log.info("Inside validateToken :: encodedString {}", encodedString);
        String decodeString = decrypt(encodedString);
        log.info("Inside validateToken :: decodeString {}", decodeString);
        if (!decodeString.equals("ElectricityVendor" + ":" + "STANZA@123")) {
            log.error("Invalid Token For Request");
            throw new UnauthorisedException("Invalid token");
        }
        return true;
    }

}
