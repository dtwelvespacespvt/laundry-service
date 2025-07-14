package com.stanzaliving.secure.authMechanism.impl;


import com.stanzaliving.secure.authMechanism.AuthStrategy;
import com.stanzaliving.secure.dto.ValidateTokenRequestDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@Log4j2
public class JwtValidation implements AuthStrategy {

    public static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60 * 1000;
    public static final long REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    @Value("${jwt.secret.key}")
    private String secret;

    @Override
    public boolean validateToken(ValidateTokenRequestDto validateTokenRequestDto) {
        try {
            String secretToken = validateTokenRequestDto.getSecretToken();
            String clientId = getUsernameFromToken(secretToken);
            return (clientId.equals(validateTokenRequestDto.getUserId()) && !isTokenExpired(secretToken));
        }catch(Exception ex){
            log.error("Invalid Token , user{}", validateTokenRequestDto.getUserId());
        }
        return false;
    }
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}