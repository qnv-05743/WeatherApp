package com.androdocs.weatherapp.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.androdocs.weatherapp.R;
import com.androdocs.weatherapp.common.Common;
import com.androdocs.weatherapp.model.WeatherResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

import static com.androdocs.weatherapp.common.Common.APP_ID;
import static com.androdocs.weatherapp.common.Common.CITY;
import static com.androdocs.weatherapp.common.Common.IMAGE_FOMAT;
import static com.androdocs.weatherapp.common.Common.IMAGE_LOAD;
import static com.androdocs.weatherapp.common.Common.KEY;
import static com.androdocs.weatherapp.common.Common.URL;

public class CityActivity extends AppCompatActivity {
    private MaterialSearchBar search_bar;
    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, errorText;
    private List<String> listCity;
    ProgressBar loader;
    private RelativeLayout mainContainer;
    private ImageView img_status;
    final Handler mHandler = new Handler();
    private Thread mUiThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        new loadCity().execute();
        search_bar.setEnabled(false);
    }

    private void initView() {
        img_status = (ImageView) findViewById(R.id.img_status);
        loader = findViewById(R.id.loader);
        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);
        search_bar = (MaterialSearchBar) findViewById(R.id.search_bar);
        addressTxt = findViewById(R.id.address);
        addressTxt.setText("");
        updated_atTxt = findViewById(R.id.updated_at);
        updated_atTxt.setText("");
        statusTxt = findViewById(R.id.status);
        statusTxt.setText("");
        tempTxt = findViewById(R.id.temp);
        tempTxt.setText("");
        temp_minTxt = findViewById(R.id.temp_min);
        temp_minTxt.setText("");
        temp_maxTxt = findViewById(R.id.temp_max);
        temp_maxTxt.setText("");
        sunriseTxt = findViewById(R.id.sunrise);
        sunriseTxt.setText("");
        sunsetTxt = findViewById(R.id.sunset);
        sunsetTxt.setText("");
        windTxt = findViewById(R.id.wind);
        windTxt.setText("");
        pressureTxt = findViewById(R.id.pressure);
        pressureTxt.setText("");
        humidityTxt = findViewById(R.id.humidity);
        humidityTxt.setText("");
        errorText = findViewById(R.id.errorText);
        errorText.setText("");

    }

    private void api_key(String City) {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL + City + KEY + APP_ID)
                .get()
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {

            final Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObj = new JSONObject(responseData);
                        JSONObject main = jsonObj.getJSONObject("main");
                        JSONObject sys = jsonObj.getJSONObject("sys");
                        JSONObject wind = jsonObj.getJSONObject("wind");
                        JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                        String image = weather.getString("icon");
                        String url = IMAGE_LOAD + image + IMAGE_FOMAT;
                        Long updatedAt = jsonObj.getLong("dt");
                        String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                        String temp = main.getString("temp") + "°C";
                        String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                        String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                        String pressure = main.getString("pressure");
                        String humidity = main.getString("humidity");
                        Long sunrise = sys.getLong("sunrise");
                        Long sunset = sys.getLong("sunset");
                        String windSpeed = wind.getString("speed");
                        String weatherDescription = weather.getString("description");
                        String address = jsonObj.getString("name") + ", " + sys.getString("country");
                        /* Populating extracted data into our views */
                        setText(addressTxt, address);
                        // addressTxt.setText(address);
                        setText(updated_atTxt, updatedAtText);
                        setText(statusTxt, weatherDescription.toUpperCase());
                        setText(tempTxt, temp);
                        setText(temp_minTxt, tempMin);
                        setText(temp_maxTxt, tempMax);
                        setText(sunriseTxt, new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                        setText(sunsetTxt, new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                        setText(windTxt, windSpeed);
                        setText(pressureTxt, pressure);
                        setText(humidityTxt, humidity);

//                        Picasso.with(getApplicationContext())
//                                .load(url)
//                                .error(R.drawable.ic_error_outline_white_24dp).into(img_status);
                    } catch (JSONException e) {

                    }


                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    private void setText(final TextView text, final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }


    private class loadCity extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackgroundSimple() {
            listCity = new ArrayList<>();

            try {
                StringBuilder builder = new StringBuilder();
                InputStream is = getResources().openRawResource(R.raw.city_list);

                GZIPInputStream gzipInputStream = new GZIPInputStream(is);
                InputStreamReader reader = new InputStreamReader(gzipInputStream);
                BufferedReader in = new BufferedReader(reader);
                String readed;
                while ((readed = in.readLine()) != null)
                    builder.append(readed);
                listCity = new Gson().fromJson(builder.toString(), new TypeToken<List<String>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return listCity;
        }

        @Override
        protected void onSuccess(final List<String> listCitys) {
            super.onSuccess(listCitys);
            search_bar.setEnabled(true);
            search_bar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest = new ArrayList<>();
                    for (String search : listCitys) {
                        if (search.toLowerCase().contains(search_bar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    search_bar.setLastSuggestions(suggest);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            search_bar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {
                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(), 0);

                    api_key(text.toString());
                    search_bar.setLastSuggestions(listCitys);
                    mainContainer.setVisibility(View.VISIBLE);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });
            search_bar.setLastSuggestions(listCity);
            // findViewById(R.id.mainContainer).setVisibility(View.GONE);
        }
    }

}
