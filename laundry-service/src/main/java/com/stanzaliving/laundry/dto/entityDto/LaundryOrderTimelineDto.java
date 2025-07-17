package com.stanzaliving.laundry.dto.entityDto;

import com.stanzaliving.laundry.enums.BagType;
import com.stanzaliving.laundry.enums.OrderStatus;
import com.stanzaliving.laundry.enums.PaymentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LaundryOrderTimelineDto extends AbstractEntityDto {
    private String residentId;
    private String residenceId;
    private String orderId;
    private String bagId;
    private BagType bagType;
    private Map<String, Object> itemDetails;
    private Double itemCount;
    private Double weight;
    private Double amount;
    private OrderStatus orderStatus;
    private String subStatus;
    private LocalDate date;
    private PaymentStatus paymentStatus;
}
