package com.example.weatherapp;

import java.util.Locale;

public class WeatherAppConstants {
    public static String EMPTY_STRING = "";
    public static String PROTOCOL = "https:";
    public static String CITY_NAME = "cityName";
    public static String ERROR_EMPTY_CITY = "Please enter city name";
    public static String ERROR_INVALID_CITY = "Please enter valid city name";
    public static String WEATHER_URL_START = "https://api.weatherapi.com/v1/forecast.json?key=6179181bf38547ceb6570417231107 &q=";
    public static String WEATHER_URL_END = "&days=1&aqi=no&alerts=no";
    public static String TODAYS_FORECAST = "Today's Weather Forecast";
    public static String MORNING_IMAGE = "https://images.unsplash.com/photo-1566228015668-4c45dbc4e2f5?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=";
    public static String NIGHT_IMAGE = "https://images.unsplash.com/photo-1532074534361-bb09a38cf917?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=";
    public static String CURRENT_OBJECT = "current";
    public static String CONDITION_OBJECT = "condition";
    public static String FORECAST_OBJECT = "forecast";
    public static String FORECAST_DAY_ARRAY = "forecastday";
    public static String HOUR_ARRAY = "hour";
    public static String DEGREE_CELCIUS = "°c";
    public static String TEMPERATURE_CELCIUS_KEY = "temp_c";
    public static String IS_DAY_KEY = "is_day";
    public static String TEXT_KEY = "text";
    public static String ICON_KEY = "icon";
    public static String TIME_KEY = "time";
    public static String WIND_SPEED_KEY = "wind_kph";
    public static String WIND_SPEED_UNIT = "Km/h";
    public static String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm";
    public static String TIME_FORMAT = "hh:mm aa";

    public static int INDEX_0 = 0;
    public static int INDEX_1 = 1;

    public static Locale DEFAULT_LOCALE =  Locale.getDefault(Locale.Category.FORMAT);
}
