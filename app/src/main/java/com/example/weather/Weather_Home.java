package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.api.Api;
import com.example.weather.api.FindLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Weather_Home extends AppCompatActivity {

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
            nextHourWeather;

    static String lat, lon;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onStart() {
        super.onStart();


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


                        lat=Double.toString(latitude);
                        lon=Double.toString(longitude);

                        FindLocation fl=new FindLocation();

                        String currLocation=fl.getAddressFromLocation(getApplicationContext(),latitude,longitude);
                        String[] splittedLoc=currLocation.split(",");

                        localCity.setText(splittedLoc[0]);
                        division.setText(splittedLoc[1]);
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


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Hello Brother", Toast.LENGTH_SHORT).show();


                if(lat.isEmpty() || lon.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Null lat lon",Toast.LENGTH_LONG).show();

                }else{
                    refreshPage(lat,lon);

                }


            }
        });


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


    private void refreshPage(String latitude, String longitude) {

        try{
        Api api=new Api();
        JsonNode days=api.callAPI(latitude, longitude);

        Toast.makeText(getApplicationContext(),days.size(),Toast.LENGTH_LONG).show();

//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        if (days.isArray() && days.size() > 0) {
//            JsonNode today = days.get(0);
//
//            // get data sorted by day
//            String todayTime = today.get("datetime").asText();
//            String todayAvgTemperature = convertToCelsius(today.get("temp").asText()); // Convert to Celsius
//            String todayMaxTemperature = convertToCelsius(today.get("tempmax").asText()); // Convert to Celsius
//            String todayMinTemperature = convertToCelsius(today.get("tempmin").asText()); // Convert to Celsius
//            String todayFeelsLikeTemperature = convertToCelsius(today.get("feelslike").asText()); // Convert to Celsius
//            String todayHumidity = Integer.toString((int) today.get("humidity").asDouble());
//            String todayPressure = Integer.toString((int) today.get("pressure").asDouble());
//            String condition = today.get("conditions").asText();
//            String windSpeed = decimalFormat.format(today.get("windspeed").asDouble()).toString();
//            String icon = today.get("icon").asText();
//            String details=today.get("details").asText();
//            // Check if "hours" is present and not null within the first day's data
//            JsonNode hours = today.get("hours");
//            if (hours != null && hours.isArray() && hours.size() > 0) {
//                int index;
//                for (int i = 0; i < hours.size(); i++) {
//                    JsonNode current = hours.get(i);
//                    String cTime = current.get("datetime").asText();
//                    String[] splitTime = cTime.split(":");
//                    String hour_current_eq = splitTime[0].trim();
//
//                    LocalTime currentTime = LocalTime.now();
//                    int currentHour = currentTime.getHour();
//                    String currentHourString = Integer.toString(currentHour).trim();
//
//                    if (currentHourString.equals(hour_current_eq)) {
//                        String currentHourWindSpeed = decimalFormat.format(current.get("windspeed").asDouble()).toString();
//                        String currentHourTemperature = convertToCelsius(current.get("temp").asText()); // Convert to Celsius
//                        index = i;
//                        String previousHourTemperature = convertToCelsius(hours.get(index - 1).get("temp").asText()); // Convert to Celsius
//                        String nextHourTemperature = convertToCelsius(hours.get(index + 1).get("temp").asText()); // Convert to Celsius
//
//                        LocalDate currentDate = LocalDate.now(); // Get the current date
//
//                        // Define a custom date format
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd", Locale.ENGLISH);
//
//                        String formattedDate = currentDate.format(formatter);
////                        temperatureStatus.setText(details);
////                        todayTemperature.setText(todayAvgTemperature+"°C");
////                        dateToday.setText(formattedDate);
////                        pressureData.setText(todayPressure+"hPa");
////                        windData.setText(windSpeed+"Km/h");
////                        humidityData.setText(todayHumidity+"%");
////                        feelsLikeData.setText(todayFeelsLikeTemperature+"°C");
////                        maxTempData.setText(todayMaxTemperature+"°C");
////                        minTempData.setText(todayMinTemperature+"°C");
////                        currentWindSpeed.setText(currentHourWindSpeed+"Km/h");
////                        previousHourWeather.setText(previousHourTemperature+"°C");
////                        currentHourWeather.setText(currentHourTemperature+"°C");
////                        nextHourWeather.setText(nextHourTemperature+"°C");
////
////                        temperatureStatus.setText("Heda");
//
//
//                    }
//                }
//            } else {
//                System.out.println("Hourly data for the first day is not available.");
//            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
//    }

    private String convertToCelsius(String fahrenheit) {
        double f = Double.parseDouble(fahrenheit);
        double celsius = (f - 32) * 5 / 9;

        DecimalFormat decimalFormat = new DecimalFormat("0.00"); // Format with 2 decimal places
        return decimalFormat.format(celsius).trim();
    }
}