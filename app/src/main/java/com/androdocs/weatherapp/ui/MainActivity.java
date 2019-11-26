package com.androdocs.weatherapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androdocs.httprequest.HttpRequest;
import com.androdocs.weatherapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.androdocs.weatherapp.common.Common.APP_ID;
import static com.androdocs.weatherapp.common.Common.CITY;
import static com.androdocs.weatherapp.common.Common.COUNTRY;
import static com.androdocs.weatherapp.common.Common.DATE_TIME;
import static com.androdocs.weatherapp.common.Common.DESCRIPTION;
import static com.androdocs.weatherapp.common.Common.HUMIDITY;
import static com.androdocs.weatherapp.common.Common.ICON;
import static com.androdocs.weatherapp.common.Common.IMAGE_FOMAT;
import static com.androdocs.weatherapp.common.Common.IMAGE_LOAD;
import static com.androdocs.weatherapp.common.Common.KEY;
import static com.androdocs.weatherapp.common.Common.MAIN;
import static com.androdocs.weatherapp.common.Common.MAX_TEMP;
import static com.androdocs.weatherapp.common.Common.MEASURE;
import static com.androdocs.weatherapp.common.Common.MIN_TEMP;
import static com.androdocs.weatherapp.common.Common.NAME;
import static com.androdocs.weatherapp.common.Common.PRESSURE;
import static com.androdocs.weatherapp.common.Common.SPEED;
import static com.androdocs.weatherapp.common.Common.SUNRISE;
import static com.androdocs.weatherapp.common.Common.SUNSET;
import static com.androdocs.weatherapp.common.Common.SYS;
import static com.androdocs.weatherapp.common.Common.TEMP;
import static com.androdocs.weatherapp.common.Common.TEMP_MAX;
import static com.androdocs.weatherapp.common.Common.TEMP_MIN;
import static com.androdocs.weatherapp.common.Common.UPDATE;
import static com.androdocs.weatherapp.common.Common.URL;
import static com.androdocs.weatherapp.common.Common.URL_LOCATION;
import static com.androdocs.weatherapp.common.Common.WEATHER;
import static com.androdocs.weatherapp.common.Common.WIND;
import static com.androdocs.weatherapp.common.Common.convertUnixToDate;
import static com.androdocs.weatherapp.common.Common.convertUnixToHour;

public class MainActivity extends AppCompatActivity {
    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, btn_choose;
    private ImageView img_status;
    private RelativeLayout relative_main;
    private String location_city;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    location_city = "lat=" + location.getLatitude() + "&" + "lon=" + location.getLongitude();
                                }
                                new weatherTask().execute();
                                relative_main.setVisibility(View.VISIBLE);
                            }
                        }
                );
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Thông báo!").
                        setMessage("GPS không được kích hoạt. Bạn cần bật GPS trong cài đặt!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }

                }).setNegativeButton("Cancel", null);
                dialog.show();
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            location_city = "lat=" + mLastLocation.getLatitude() + "&" + "lon=" + mLastLocation.getLongitude();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    private void initView() {
        relative_main = (RelativeLayout) findViewById(R.id.relative_main);
        img_status = (ImageView) findViewById(R.id.img_status);
        btn_choose = findViewById(R.id.btn_choose);
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
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CityActivity.class);
                startActivity(intent);
            }
        });


    }


    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet(URL_LOCATION + location_city + KEY + APP_ID);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject(MAIN);
                JSONObject sys = jsonObj.getJSONObject(SYS);
                JSONObject wind = jsonObj.getJSONObject(WIND);
                JSONObject weather = jsonObj.getJSONArray(WEATHER).getJSONObject(0);
                Long updatedAt = jsonObj.getLong(DATE_TIME);
                String updatedAtText = UPDATE + convertUnixToDate(updatedAt);
                String temp = main.getString(TEMP) + MEASURE;
                String tempMin = MIN_TEMP + main.getString(TEMP_MIN) + MEASURE;
                String tempMax = MAX_TEMP + main.getString(TEMP_MAX) + MEASURE;
                String pressure = main.getString(PRESSURE);
                String humidity = main.getString(HUMIDITY);
                String image = weather.getString(ICON);
                String url = IMAGE_LOAD + image + IMAGE_FOMAT;
                Long sunrise = sys.getLong(SUNRISE);
                Long sunset = sys.getLong(SUNSET);
                String windSpeed = wind.getString(SPEED);
                String weatherDescription = weather.getString(DESCRIPTION);
                String address = jsonObj.getString(NAME) + ", " + sys.getString(COUNTRY);
                /* Populating extracted data into our views */
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(convertUnixToHour(sunrise));
                sunsetTxt.setText(convertUnixToHour(sunset));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);
                Picasso.with(img_status.getContext()).load(url)
                        .error(R.drawable.ic_error_outline_white_24dp).into(img_status);
                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }

    }
}
