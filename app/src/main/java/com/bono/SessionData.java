package com.bono;

import java.util.List;

public class SessionData {
    private List<IntervalData> IntervalDatas;
    private Float TravelledDistance;
    private Float MaxSpeed;
    private String ExpiredTime;

    public List<IntervalData> getIntervalDatas() {
        return IntervalDatas;
    }

    public void setIntervalDatas(List<IntervalData> intervalDatas) {
        IntervalDatas = intervalDatas;
    }

    public Float getTravelledDistance() {
        return TravelledDistance;
    }

    public void setTravelledDistance(Float travelledDistance) {
        TravelledDistance = travelledDistance;
    }

    public Float getMaxSpeed() {
        return MaxSpeed;
    }

    public void setMaxSpeed(Float maxSpeed) {
        MaxSpeed = maxSpeed;
    }

    public String getExpiredTime() {
        return ExpiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        ExpiredTime = expiredTime;
    }
}
