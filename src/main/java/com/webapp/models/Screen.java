package com.webapp.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.webapp.enums.Dimension;
import com.webapp.enums.SeatType;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Screen {
	private String screenId;
	@NotEmpty
	private String theaterId;
	@NotEmpty
	private String screenName;
	@NotNull
	private Dimension dimension;
	private List<ScreenTime> screenTimes = new ArrayList<>();
	@NotNull
	@Min(3)
	private Integer columns;
	@NotEmpty
	@Size(min = 3,max = 3)
	private Map<SeatType, Integer> noOfRowsPerSeatType;
	@NotEmpty
	@Size(min = 3,max = 3)
	private Map<SeatType, Integer> priceOfDifferentSeatType;
}
