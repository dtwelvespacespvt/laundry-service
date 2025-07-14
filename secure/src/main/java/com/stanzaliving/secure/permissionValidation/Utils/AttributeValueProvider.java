package com.stanzaliving.secure.permissionValidation.Utils;




import com.stanzaliving.secure.permissionValidation.AttributeDto;

import javax.servlet.http.HttpServletRequest;

public abstract class AttributeValueProvider {

    public abstract AttributeDto fillAttributeValues(HttpServletRequest request);

}
