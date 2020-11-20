package com.webapp.models;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class ShowDate {
	private Date moviePlayingFromDate;
	private Date moviePlayingToDate;
	private List<ScreenTime> screenTimes;
}
