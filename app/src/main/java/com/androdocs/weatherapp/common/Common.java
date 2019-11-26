package com.androdocs.weatherapp.common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Common {
    public static String URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String URL_LOCATION = "https://api.openweathermap.org/data/2.5/weather?";
    public static String CITY = "Ha Noi";
    public static String KEY = "&units=metric&appid=";
    public static String APP_ID = "ea9c047e5524a3eb5c1d63eda8f924e9";
    public static String IMAGE_LOAD = "https://openweathermap.org/img/wn/";
    public static String IMAGE_FOMAT = "@2x.png";
    public static Location current_location = null;

    ///////
    public static String UPDATE = "Updated at: ";
    public static String MIN_TEMP = "Min Temp: ";
    public static String MAX_TEMP = "Max Temp: ";
    public static String TEMP = "temp";
    public static String WEATHER = "weather";
    public static String MEASURE = "°C";
    public static String TEMP_MIN = "temp_min";
    public static String TEMP_MAX = "temp_max";
    public static String PRESSURE = "pressure";
    public static String HUMIDITY = "humidity";
    public static String SUNRISE = "sunrise";
    public static String SUNSET = "sunset";
    public static String SPEED = "speed";
    public static String WIND = "wind";
    public static String DESCRIPTION = "description";
    public static String NAME = "name";
    public static String COUNTRY = "country";
    public static String MAIN = "main";
    public static String SYS = "sys";
    public static String ICON = "icon";
    public static String DATE_TIME = "dt";

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(date);
        return formatted;
    }
}
