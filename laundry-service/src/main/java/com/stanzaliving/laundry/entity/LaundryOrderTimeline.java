package com.stanzaliving.laundry.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.stanzaliving.laundry.annotation.DynamodbTableEnv;
import com.stanzaliving.laundry.enums.BagType;
import com.stanzaliving.laundry.enums.OrderStatus;
import com.stanzaliving.laundry.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DynamoDBTable(tableName = "")
@DynamodbTableEnv("dynamodb.laundry.order.timeline.table")
public class LaundryOrderTimeline extends AbstractDynamoEntity {

    @DynamoDBAttribute
    private String residentId;

    @DynamoDBAttribute
    private String residenceId;

    @DynamoDBAttribute
    private String orderId;

    @DynamoDBAttribute
    private String bagId;

    @DynamoDBAttribute
    private BagType bagType;

    @DynamoDBAttribute
    private Map<String, Object> itemDetails;

    @DynamoDBAttribute
    private Double itemCount;

    @DynamoDBAttribute
    private Double weight;

    @DynamoDBAttribute
    private Double amount;

    @DynamoDBAttribute
    private OrderStatus orderStatus;

    @DynamoDBAttribute
    private String subStatus;

    @DynamoDBAttribute
    private LocalDate date;

    @DynamoDBAttribute
    private PaymentStatus paymentStatus;
}
