package org.app.gtsconnected3.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewTripForm {
    String departurePlace;
    LocalDateTime departureTime;
    String arrivalPlace;
    LocalDateTime arrivalTime;
    String note;
}
