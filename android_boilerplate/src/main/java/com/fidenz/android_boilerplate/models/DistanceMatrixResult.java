package com.fidenz.android_boilerplate.models;

public class DistanceMatrixResult {
    private String distanceString, timeString;
    private int  timeValue, timeSecondValue;
    private double distanceValue,distanceInMeters;


    public DistanceMatrixResult(String distanceString, String timeString, double distanceValue, int timeValue, int timeSecondValue, double distanceInMeters) {
        this.distanceString = distanceString;
        this.timeString = timeString;
        this.distanceValue = distanceValue;
        this.timeValue = timeValue;
        this.timeSecondValue = timeSecondValue;
        this.distanceInMeters = distanceInMeters;
    }


    public String getDistanceString() {
        return distanceString;
    }

    public void setDistanceString(String distanceString) {
        this.distanceString = distanceString;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public double getDistanceInKM() {
        return distanceValue;
    }

    public void setDistanceValue(double distanceValue) {
        this.distanceValue = distanceValue;
    }

    public int getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(int timeValue) {
        this.timeValue = timeValue;
    }

    public int getTimeSecondValue() {
        return timeSecondValue;
    }

    public void setTimeSecondValue(int timeSecondValue) {
        this.timeSecondValue = timeSecondValue;
    }

    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }
}
