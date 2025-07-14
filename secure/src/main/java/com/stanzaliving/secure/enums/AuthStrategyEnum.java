package com.stanzaliving.secure.enums;


public enum AuthStrategyEnum {

    AES, JWT, BASIC;

    public static AuthStrategyEnum fromAuthStrategy(String authStrategy) {
        for (AuthStrategyEnum b : AuthStrategyEnum.values()) {
            if (b.name().equalsIgnoreCase(authStrategy)) {
                return b;
            }
        }
        return null;
    }
}
