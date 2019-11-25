package com.androdocs.weatherapp.common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Common {
    public static String URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String CITY = "Ha Noi";
    public static String KEY = "&units=metric&appid=";
    public static String APP_ID = "ea9c047e5524a3eb5c1d63eda8f924e9";
    public static String IMAGE_LOAD = "https://openweathermap.org/img/wn/";
    public static String IMAGE_FOMAT = "@2x.png";
    public static Location curent_location = null;
    public static String UPDATE = "Updated at: ";
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
