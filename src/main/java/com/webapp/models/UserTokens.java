package com.webapp.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "UserTokens")
public class UserTokens {
    @DynamoDBHashKey
    private String userName;
    @DynamoDBAttribute
    private String idToken;
    @DynamoDBAttribute
    private String accessToken;
    @DynamoDBAttribute
    private String refreshToken;
}
