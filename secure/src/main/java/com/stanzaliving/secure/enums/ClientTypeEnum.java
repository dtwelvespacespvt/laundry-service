package com.stanzaliving.secure.enums;

public enum ClientTypeEnum {
    S2S, THIRD_PARTY, USER;

    public static ClientTypeEnum fromClientType(String clientType) {
        for (ClientTypeEnum b : ClientTypeEnum.values()) {
            if (b.name().equalsIgnoreCase(clientType)) {
                return b;
            }
        }
        return null;
    }
}
