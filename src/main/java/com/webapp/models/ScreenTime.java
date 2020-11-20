package com.webapp.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.webapp.enums.ShowTime;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ScreenTime {
	@NotBlank
	private ShowTime showTiming;
	@NotBlank
	@DynamoDBAttribute
	private String movieId;
	@NotBlank
	@DynamoDBAttribute
	private String seatPlanId;
	@NotNull
	@DynamoDBAttribute
	private Date moviePlayingFromDate;
	@NotNull
	@DynamoDBAttribute
	private Date moviePlayingToDate;
}
