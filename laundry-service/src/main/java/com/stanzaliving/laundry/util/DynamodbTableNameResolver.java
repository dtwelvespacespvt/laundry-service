package com.stanzaliving.laundry.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.stanzaliving.laundry.annotation.DynamodbTableEnv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DynamodbTableNameResolver extends DynamoDBMapperConfig.DefaultTableNameResolver {

    private Environment environment = null;

    @Override
    public String getTableName(Class<?> clazz, DynamoDBMapperConfig dynamoDBMapperConfig) {

        if (environment == null) {
            return null;
        }
        String tableNameToReturn;
        DynamoDBTable dynamoDBTable = clazz.getAnnotation(DynamoDBTable.class);
        if (dynamoDBTable == null) {
            throw new RuntimeException("Not a dynamodb table to resolve name");
        }
        tableNameToReturn = dynamoDBTable.tableName();
        DynamodbTableEnv dynamodbTableEnv = clazz.getAnnotation(DynamodbTableEnv.class);
        if(dynamoDBTable.tableName().isEmpty() && (dynamodbTableEnv==null || dynamodbTableEnv.value().isEmpty())){
            throw new RuntimeException("Table name in DynamoDBTable or DynamodbTableEnv is mandatory");
        }
        if (dynamodbTableEnv == null) {
            return null;
        } else {
            return environment.getProperty(dynamodbTableEnv.value());
        }
    }

}
