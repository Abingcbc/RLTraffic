package org.sse.cbc.car.domain;

import java.io.Serializable;

/**
 * Created by ABINGCBC
 * on 2020-04-30
 */
public class TrafficOrder implements Serializable {
    public Integer reqId;
    public String timestamp;
    public Double passengerLatitude;
    public Double passengerLongitude;
    public Double destinationLatitude;
    public Double destinationLongitude;
    public Double driverLatitude;
    public Double driverLongitude;

}
