package com.stanzaliving.laundry.dto.entityDto;

import com.stanzaliving.laundry.enums.PaymentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LaundryPaymentDto extends AbstractEntityDto {
    private String residentId;
    private String transactionId;
    private Double amount;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDate date;
}
