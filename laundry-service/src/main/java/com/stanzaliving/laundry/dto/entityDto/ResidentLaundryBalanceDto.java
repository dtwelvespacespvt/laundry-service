package com.stanzaliving.laundry.dto.entityDto;

import com.stanzaliving.laundry.enums.BagType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ResidentLaundryBalanceDto extends AbstractEntityDto {
    private String residentId;
    private BagType bagType;
    private Integer bagPurchased;
    private Integer bagsScanned;
}
