package com.example.weather.api;

public class WeatherData {
    private String temperature;
    private String windSpeed;
    private String pressure;
    private String minTemperature;
    private String maxTemperature;
    private String status;
    private String humidity;
    private String feelsLike;
    private String sunrise;
    private String sunset;
    private String currentHourTemp;
    private String previousHourTemp;
    private String nextHourTemp;
    private String currentHourWindSpeed;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentHourTemp() {
        return currentHourTemp;
    }

    public void setCurrentHourTemp(String currentHourTemp) {
        this.currentHourTemp = currentHourTemp;
    }

    public String getPreviousHourTemp() {
        return previousHourTemp;
    }

    public void setPreviousHourTemp(String previousHourTemp) {
        this.previousHourTemp = previousHourTemp;
    }

    public String getNextHourTemp() {
        return nextHourTemp;
    }

    public void setNextHourTemp(String nextHourTemp) {
        this.nextHourTemp = nextHourTemp;
    }

    public String getCurrentHourWindSpeed() {
        return currentHourWindSpeed;
    }

    public void setCurrentHourWindSpeed(String currentHourWindSpeed) {
        this.currentHourWindSpeed = currentHourWindSpeed;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }
}
