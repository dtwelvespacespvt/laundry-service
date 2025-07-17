package com.stanzaliving.laundry.dto.entityDto;

import com.stanzaliving.laundry.enums.BagType;
import com.stanzaliving.laundry.enums.PaymentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ResidentBagPurchaseDto extends AbstractEntityDto {
    private String residentId;
    private String transactionId;
    private BagType bagType;
    private Integer quantity;
    private Double amountPaid;
    private LocalDate date;
    private PaymentStatus paymentStatus;
}
