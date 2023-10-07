package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.api.Api;
import com.example.weather.api.WeatherData;
import com.example.weather.hourly.WeatherApiHourly;
import com.example.weather.hourly.WeatherDataHourly;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Weather_Home extends AppCompatActivity implements Api.ApiCallback {

    private ImageButton reload;
    private TextView localCity,
            division,
            todayTemperature,
            temperatureStatus,
            dateToday,
            pressureData,
            windData,
            humidityData,
            feelsLikeData,
            maxTempData,
            minTempData,
            currentWindSpeed,
            currentHourWeather,
            previousHourWeather,
            nextHourWeather,previousHour,currentHour,nextHour;

    private Dialog loadingDialog;

    static double lat, lon;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
//    private LocationUtility locationUtility;

    @Override
    protected void onStart() {
        super.onStart();
        showLoadingDialog();
        refreshUi();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);
        reload = findViewById(R.id.reloadButton);
        localCity = findViewById(R.id.cityText);
        division = findViewById(R.id.locationDivision);
        todayTemperature = findViewById(R.id.currentTemperature);
        temperatureStatus = findViewById(R.id.currentStatus);
        dateToday = findViewById(R.id.currentDate);
        pressureData = findViewById(R.id.pressureData);
        windData = findViewById(R.id.windData);
        humidityData = findViewById(R.id.humidity);
        feelsLikeData = findViewById(R.id.feelsLikeTemperature);
        maxTempData = findViewById(R.id.maxTempData);
        minTempData = findViewById(R.id.minTempData);
        currentWindSpeed = findViewById(R.id.currentWindSpeed);
        currentHourWeather = findViewById(R.id.currentHourWeather);
        previousHourWeather = findViewById(R.id.previousHourWeather);
        nextHourWeather = findViewById(R.id.nextHourWeather);
        currentHour=findViewById(R.id.currentHour);
        nextHour=findViewById(R.id.nextHour);
        previousHour=findViewById(R.id.previousHour);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Refresh button onclick
                showLoadingDialog();
                refreshUi();
            }
        });
    }

    void refreshUi() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        lat=latitude;
                        lon=longitude;

                        // Call the Api class with the callback
                        Api api = new Api(latitude, longitude, Weather_Home.this);
                        api.execute();

                        lat=latitude;
                        lon=longitude;
                    }
                }
            }
        };

        // Check and request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationUpdates();
        }
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(this);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loadingDialog.setContentView(R.layout.loading_layout);
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied. Cannot fetch location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    // Implement the callback method to update UI with fetched data
    @Override
    public void onDataFetched(WeatherData data) {


        LocalDate currentDate = LocalDate.now();

        // Define a custom date format
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd", Locale.ENGLISH);

        // Format the date in the desired format
        String formattedDate = currentDate.format(customFormatter);
        hideLoadingDialog();
        if (data != null) {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

            String cTime=sdf.format(calendar.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            String pTime=sdf.format(calendar.getTime());
            calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            String nTime=sdf.format(calendar.getTime());

            todayTemperature.setText(data.getTemperature()+"°C");
            windData.setText(data.getWindSpeed()+" mph");
            pressureData.setText(data.getPressure()+" hPa");
            minTempData.setText(data.getSunrise());
            maxTempData.setText(data.getSunset());
            temperatureStatus.setText(data.getStatus());
            humidityData.setText(data.getHumidity()+"%");
            feelsLikeData.setText(data.getFeelsLike()+"°C");
            dateToday.setText(formattedDate);


            currentWindSpeed.setText(data.getCurrentHourWindSpeed()+" mph");
            currentHourWeather.setText(data.getCurrentHourTemp()+"°C");
            previousHourWeather.setText(data.getPreviousHourTemp()+"°C");
            nextHourWeather.setText(data.getPreviousHourTemp()+"°C");

            currentHour.setText(getHourAndAMPM(cTime));
            previousHour.setText(getHourAndAMPM(pTime));
            nextHour.setText(getHourAndAMPM(nTime));


            WeatherApiHourly.getHourlyWeather(lat,lon, new WeatherApiHourly.WeatherResponseListener() {
                @Override
                public void onWeatherDataReceived(WeatherDataHourly weatherData) {
                    // Update the UI with the retrieved weather data
                    currentHourWeather.setText(convertCelsiusFromKelvin(weatherData.getCurrentHourTemperature()) + "°C");
                    nextHourWeather.setText(convertCelsiusFromKelvin(weatherData.getNextHourTemperature()) + "°C");
                    previousHourWeather.setText(convertCelsiusFromKelvin(weatherData.getPreviousHourTemperature()) + "°C");
                    currentWindSpeed.setText(weatherData.getWindSpeedCurrentHour()+" mph");
                }
            });
        }
    }

    private String getHourAndAMPM(String time) {
        return time.substring(0, 2) + ":00 " + time.substring(6);
    }

    String convertCelsiusFromKelvin(double kelvin) {
        double temperatureCelsius = kelvin - 273.15;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(temperatureCelsius);
    }
}
