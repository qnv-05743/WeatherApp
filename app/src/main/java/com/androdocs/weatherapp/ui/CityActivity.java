package com.androdocs.weatherapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.androdocs.weatherapp.R;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class CityActivity extends AppCompatActivity {
    private MaterialSearchBar search_bar;
    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, btn_choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
    }

    private void initView() {
        search_bar = findViewById(R.id.search_bar);
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
    }
}
