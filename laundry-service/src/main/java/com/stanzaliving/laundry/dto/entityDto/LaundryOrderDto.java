package com.stanzaliving.laundry.dto.entityDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LaundryOrderDto extends AbstractEntityDto {

}
