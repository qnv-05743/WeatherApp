package com.androdocs.weatherapp.Retrofit;

import com.androdocs.weatherapp.model.WeatherResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Call<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                             @Query("appid") String appid,
                                             @Query("units") String unit);
}
