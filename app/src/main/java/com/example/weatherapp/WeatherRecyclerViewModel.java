package com.example.weatherapp;

public class WeatherRecyclerViewModel {
    private final String time;
    private final String temperature;
    private final String icon;
    private final String windSpeed;

    public WeatherRecyclerViewModel(String time, String temperature, String icon, String windSpeed) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }
}
