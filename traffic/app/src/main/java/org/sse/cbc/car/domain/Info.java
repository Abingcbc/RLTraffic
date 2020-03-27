package org.sse.cbc.car.domain;

/**
 * Created by ABINGCBC
 * on 2020-03-27
 */
public class Info {
    public String infoType;
    public String infoContent;
    public String infoUnit;

    public Info(String infoType, String infoContent, String infoUnit) {
        this.infoType = infoType;
        this.infoContent = infoContent;
        this.infoUnit = infoUnit;
    }
}
