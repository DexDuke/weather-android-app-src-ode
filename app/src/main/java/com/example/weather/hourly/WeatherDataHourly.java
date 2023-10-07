package com.example.weather.hourly;

public class WeatherDataHourly {
    private double currentHourTemperature;
    private double nextHourTemperature;
    private double previousHourTemperature;
    private double windSpeedCurrentHour;

    public WeatherDataHourly(double currentHourTemperature, double nextHourTemperature, double previousHourTemperature, double windSpeedCurrentHour) {
        this.currentHourTemperature = currentHourTemperature;
        this.nextHourTemperature = nextHourTemperature;
        this.previousHourTemperature = previousHourTemperature;
        this.windSpeedCurrentHour=windSpeedCurrentHour;
    }

    public double getCurrentHourTemperature() {
        return currentHourTemperature;
    }

    public double getNextHourTemperature() {
        return nextHourTemperature;
    }

    public double getWindSpeedCurrentHour() {
        return windSpeedCurrentHour;
    }

    public void setWindSpeedCurrentHour(double windSpeedCurrentHour) {
        this.windSpeedCurrentHour = windSpeedCurrentHour;
    }

    public double getPreviousHourTemperature() {
        return previousHourTemperature;
    }
}
