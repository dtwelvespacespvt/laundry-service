package com.stanzaliving.secure.dto;

import com.stanzaliving.secure.enums.AuthStrategyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ValidateTokenRequestDto {

    private String userId;
    private String secretToken;
    private AuthStrategyEnum authStrategyEnum;
}
