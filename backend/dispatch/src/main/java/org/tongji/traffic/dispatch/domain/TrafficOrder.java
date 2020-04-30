package org.tongji.traffic.dispatch.domain;

import lombok.Data;

@Data
public class TrafficOrder {

    private Integer reqId;
    private String timestamp;
    private Double passengerLatitude;
    private Double passengerLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private Double driverLatitude;
    private Double driverLongitude;

}
