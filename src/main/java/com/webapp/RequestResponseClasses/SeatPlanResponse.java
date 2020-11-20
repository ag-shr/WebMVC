package com.webapp.RequestResponseClasses;

import com.webapp.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
@RequiredArgsConstructor
public class SeatPlanResponse {
    private String seatPlanId;
    private Map<SeatType, Integer> noOfRowsPerSeatType;
    private Integer noOfColumns;
    private List<String> bookedSeats;
    private List<String> lockedSeats;
}
