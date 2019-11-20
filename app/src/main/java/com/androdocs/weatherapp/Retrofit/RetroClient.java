package com.androdocs.weatherapp.Retrofit;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null)

            instance = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return instance;
    }
}
