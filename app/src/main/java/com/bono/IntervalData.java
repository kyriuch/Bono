package com.bono;

public class IntervalData {
    private float currentSpeed;
    private float currentAltitude;
    private String currentTime;
    private float currentDistance;
    private int currentSteps;

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getCurrentAltitude() {
        return currentAltitude;
    }

    public void setCurrentAltitude(float currentAltitude) {
        this.currentAltitude = currentAltitude;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public float getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(float currentDistance) {
        this.currentDistance = currentDistance;
    }

    public int getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(int currentSteps) {
        this.currentSteps = currentSteps;
    }
}
