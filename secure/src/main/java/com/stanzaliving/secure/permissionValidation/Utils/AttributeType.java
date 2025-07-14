package com.stanzaliving.secure.permissionValidation.Utils;

import java.util.Arrays;
import java.util.List;


public enum AttributeType {
    NUMBER(Arrays.asList(Integer.class,Long.class,Double.class)),
    TEXT(Arrays.asList(String.class));

    private List<Class<?>> typeClass;

    AttributeType(List<Class<?>> typeClass){
        this.typeClass=typeClass;
    }
    public List<Class<?>> getTypeClass() {
        return typeClass;
    }

}
