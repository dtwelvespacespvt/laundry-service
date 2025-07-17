package com.stanzaliving.laundry.dto.entityDto;

import com.stanzaliving.laundry.enums.BagType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LaundryBagDto extends AbstractEntityDto {

    private String bagId;
    private BagType bagType;
    private boolean active;
}
