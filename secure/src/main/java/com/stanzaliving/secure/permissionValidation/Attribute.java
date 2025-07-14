package com.stanzaliving.secure.permissionValidation;






import com.stanzaliving.secure.permissionValidation.Utils.AttributeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Attribute {
    AttributeType type();  //change name accordingly
}
