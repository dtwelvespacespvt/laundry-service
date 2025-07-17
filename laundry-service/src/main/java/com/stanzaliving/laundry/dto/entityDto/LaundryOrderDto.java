package com.stanzaliving.laundry.dto.entityDto;

import com.stanzaliving.laundry.enums.BagType;
import com.stanzaliving.laundry.enums.LaundryPaymentStatus;
import com.stanzaliving.laundry.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LaundryOrderDto extends AbstractEntityDto {
    private String residentId;
    private String residenceId;
    private String orderId;
    private String bagId;
    private BagType bagType;
    private Integer itemCount;
    private Double weight;
    private Double amount;
    private OrderStatus orderStatus;
    private String subStatus;
    private LocalDate date;
    private LaundryPaymentStatus paymentStatus;
    private Double freeWeight;
    private Double chargeableWeight;
}
