package com.example.weatherapp;

import static androidx.core.view.WindowInsetsCompat.Type.ime;
import static androidx.core.view.WindowInsetsCompat.Type.systemBars;
import static androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;
import static com.example.weatherapp.WeatherAppConstants.CITY_NAME;
import static com.example.weatherapp.WeatherAppConstants.CONDITION_OBJECT;
import static com.example.weatherapp.WeatherAppConstants.CURRENT_OBJECT;
import static com.example.weatherapp.WeatherAppConstants.DEGREE_CELCIUS;
import static com.example.weatherapp.WeatherAppConstants.EMPTY_STRING;
import static com.example.weatherapp.WeatherAppConstants.ERROR_INVALID_CITY;
import static com.example.weatherapp.WeatherAppConstants.FORECAST_DAY_ARRAY;
import static com.example.weatherapp.WeatherAppConstants.FORECAST_OBJECT;
import static com.example.weatherapp.WeatherAppConstants.HOUR_ARRAY;
import static com.example.weatherapp.WeatherAppConstants.ICON_KEY;
import static com.example.weatherapp.WeatherAppConstants.INDEX_0;
import static com.example.weatherapp.WeatherAppConstants.INDEX_1;
import static com.example.weatherapp.WeatherAppConstants.IS_DAY_KEY;
import static com.example.weatherapp.WeatherAppConstants.PROTOCOL;
import static com.example.weatherapp.WeatherAppConstants.TEMPERATURE_CELCIUS_KEY;
import static com.example.weatherapp.WeatherAppConstants.TEXT_KEY;
import static com.example.weatherapp.WeatherAppConstants.TIME_KEY;
import static com.example.weatherapp.WeatherAppConstants.WEATHER_URL_END;
import static com.example.weatherapp.WeatherAppConstants.WEATHER_URL_START;
import static com.example.weatherapp.WeatherAppConstants.WIND_SPEED_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>,
        Response.ErrorListener {
    private ProgressBar loadingSpinner;
    private RelativeLayout mainRelativeLayout;
    private TextView cityTV, temperatureTV, conditionTV, recyclerViewHeading;
    private ImageView background, searchIcon, conditionIV;
    private RecyclerView weatherRecyclerView;
    private TextInputEditText cityET;
    private ArrayList<WeatherRecyclerViewModel> weatherRecyclerViewModels;
    private WeatherRecyclerViewAdapter adapter;
    private String prevCity = EMPTY_STRING;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        final WindowInsetsControllerCompat controller =  WindowCompat.getInsetsController(getWindow(),
                getWindow().getDecorView());
        controller.hide(systemBars());
        controller.setSystemBarsBehavior(BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        weatherRecyclerViewModels = new ArrayList<>();
        adapter = new WeatherRecyclerViewAdapter(/* context= */ this, weatherRecyclerViewModels);
        weatherRecyclerView.setAdapter(adapter);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        prevCity = sharedPref.getString(WeatherAppConstants.CITY_NAME, EMPTY_STRING);
        if (!prevCity.equals(EMPTY_STRING)) {
            showLoadingSpinner();
            getWeatherInfo(prevCity);
        }

        searchIcon.setOnClickListener(view -> {
            controller.hide(ime());
            String city = cityET.getText() != null ? cityET.getText().toString() : EMPTY_STRING;
            if (city.isEmpty()) {
                Toast.makeText(/* context= */ MainActivity.this,
                        WeatherAppConstants.ERROR_EMPTY_CITY, Toast.LENGTH_SHORT).show();
            } else {
                showLoadingSpinner();
                city = city.substring(INDEX_0, INDEX_1).toUpperCase().concat(city.substring(INDEX_1));
                cityET.setText(city);
                getWeatherInfo(city);
            }
        });
    }

    private void getWeatherInfo(String cityName) {
        final String weatherUrl = WEATHER_URL_START + cityName + WEATHER_URL_END;
        cityTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(/* context= */ MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherUrl,
                /* jsonRequest= */ null, /* listener= */ this, /* errorListener= */ this);

        requestQueue.add(jsonObjectRequest);
    }

    private void initViews() {
        loadingSpinner = findViewById(R.id.loadingSpinner);
        mainRelativeLayout = findViewById(R.id.mainRelativeLayout);
        cityTV = findViewById(R.id.cityNameTextView);
        temperatureTV = findViewById(R.id.temperatureTextView);
        conditionTV = findViewById(R.id.textViewCondition);
        background = findViewById(R.id.blackBackground);
        searchIcon = findViewById(R.id.searchIcon);
        conditionIV = findViewById(R.id.weatherCondition);
        recyclerViewHeading = findViewById(R.id.textViewForecastHeading);
        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);
        cityET = findViewById(R.id.textInputEditTextCity);
    }

    private void showLoadingSpinner() {
        background.setVisibility(View.GONE);
        mainRelativeLayout.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void hideLoadingSpinner() {
        background.setVisibility(View.VISIBLE);
        mainRelativeLayout.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(JSONObject response) {
        hideLoadingSpinner();
        recyclerViewHeading.setText(WeatherAppConstants.TODAYS_FORECAST);
        weatherRecyclerViewModels.clear();

        try {
            String temperature = response.getJSONObject(CURRENT_OBJECT).getString(TEMPERATURE_CELCIUS_KEY);
            temperatureTV.setText(temperature.concat(DEGREE_CELCIUS));
            int isDay = response.getJSONObject(CURRENT_OBJECT).getInt(IS_DAY_KEY);
            String condition = response.getJSONObject(CURRENT_OBJECT).getJSONObject(CONDITION_OBJECT).getString(TEXT_KEY);
            String conditionIcon = response.getJSONObject(CURRENT_OBJECT).getJSONObject(CONDITION_OBJECT).getString(ICON_KEY);
            Picasso.get().load(PROTOCOL.concat(conditionIcon)).into(conditionIV);
            conditionTV.setText(condition);
            if (isDay == 1) {
                Picasso.get().load(WeatherAppConstants.MORNING_IMAGE).into(background);
            } else {
                Picasso.get().load(WeatherAppConstants.NIGHT_IMAGE).into(background);
            }

            JSONObject forecastObject = response.getJSONObject(FORECAST_OBJECT);
            JSONObject forecast0 = forecastObject.getJSONArray(FORECAST_DAY_ARRAY).getJSONObject(INDEX_0);
            JSONArray hoursArray = forecast0.getJSONArray(HOUR_ARRAY);

            for (int i = 0; i < hoursArray.length(); i++) {
                JSONObject hoursObject = hoursArray.getJSONObject(i);
                weatherRecyclerViewModels.add(new WeatherRecyclerViewModel(
                        hoursObject.getString(TIME_KEY),
                        hoursObject.getString(TEMPERATURE_CELCIUS_KEY),
                        hoursObject.getJSONObject(CONDITION_OBJECT).getString(ICON_KEY),
                        hoursObject.getString(WIND_SPEED_KEY)
                ));
            }
            adapter.notifyDataSetChanged();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(CITY_NAME, cityTV.getText().toString());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(/* context= */ this, ERROR_INVALID_CITY, Toast.LENGTH_SHORT).show();
        getWeatherInfo(prevCity);
    }
}