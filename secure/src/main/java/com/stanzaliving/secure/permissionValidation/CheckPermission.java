package com.stanzaliving.secure.permissionValidation;




import com.stanzaliving.secure.permissionValidation.Utils.AttributeValueProvider;
import com.stanzaliving.secure.permissionValidation.Utils.EvaluationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
    String resource();
    String[] permissions();
    Class<? extends AttributeValueProvider> attributeValueProvider();

    EvaluationType evaluationType();
}
