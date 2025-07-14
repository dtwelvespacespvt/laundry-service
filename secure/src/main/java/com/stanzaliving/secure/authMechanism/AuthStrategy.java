package com.stanzaliving.secure.authMechanism;


import com.stanzaliving.secure.dto.ValidateTokenRequestDto;

public interface AuthStrategy {

    boolean validateToken(ValidateTokenRequestDto validateTokenRequestDto) ;
}
