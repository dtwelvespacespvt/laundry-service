package com.stanzaliving.secure.interceptor;

import com.stanzaliving.secure.error.UnauthorisedException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.Base64;

import static com.stanzaliving.secure.authMechanism.SecurityConfig.ALGORITHM;

@Log4j2
public class VendorInterceptor implements HandlerInterceptor {

    @Autowired
    private SecretKeySpec secretKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.info("Request URL {}",request.getRequestURI());
        validateToken(String.valueOf(request.getHeader("auth"))) ;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        new BaseUIDInterceptor().postHandle(request, response, handler);
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

}