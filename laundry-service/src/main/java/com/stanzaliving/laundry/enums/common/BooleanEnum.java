package com.stanzaliving.laundry.enums.common;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@DynamoDBTypeConvertedEnum
@AllArgsConstructor
@Getter
public enum BooleanEnum {
    TRUE (true),
    FALSE (false);

    private final boolean booleanData;

//    private final String booleanE;

    public static Map<Boolean, BooleanEnum> booleanDataMap = new HashMap<>();

    static {
        for(BooleanEnum booleanEnum : BooleanEnum.values()) {
            booleanDataMap.put(booleanEnum.booleanData, booleanEnum);
        }
    }

    public static BooleanEnum getBooleanData(Boolean booleanData) {
        return booleanDataMap.get(booleanData);
    }


}
