package com.example.weather.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Api extends AsyncTask<Void, Void, WeatherData> {
    private String apiKey = "108e658ece27b123c22273339290aec2";
    private double latitude;
    private double longitude;
    private WeakReference<ApiCallback> callback;

    public interface ApiCallback {
        void onDataFetched(WeatherData data);
    }

    public Api(double latitude, double longitude, ApiCallback callback) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.callback = new WeakReference<>(callback);
    }

    @Override
    protected WeatherData doInBackground(Void... voids) {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" +
                    latitude + "&lon=" + longitude + "&appid=" + apiKey;

//            String apiUrl2 = "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat="+latitude+"&lon="+longitude+"&appid=" + apiKey;


            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject jsonObject = new JSONObject(response.toString());

            // Extract the required weather information
            JSONObject mainObject = jsonObject.getJSONObject("main");
            double temperature = mainObject.getDouble("temp");
//            double minTemperature = mainObject.getDouble("temp_min");
//            double maxTemperature = mainObject.getDouble("temp_max");
            int pressure = mainObject.getInt("pressure");
            int humidity = mainObject.getInt("humidity");
            double feelsLike = mainObject.getDouble("feels_like");

            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            String description = weatherObject.getString("description");
            int id=weatherObject.getInt("id");


            JSONObject windObject = jsonObject.getJSONObject("wind");
            double windSpeed = windObject.getDouble("speed");

            JSONObject sysObject = jsonObject.getJSONObject("sys");
            long sunriseTimestamp = sysObject.getLong("sunrise");
            long sunsetTimestamp = sysObject.getLong("sunset");


            Date sunriseDate = new Date(sunriseTimestamp * 1000); // Multiply by 1000 to convert seconds to milliseconds
            Date sunsetDate = new Date(sunsetTimestamp * 1000);


            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");


            String formattedSunriseTime = dateFormat.format(sunriseDate);
            String formattedSunsetTime = dateFormat.format(sunsetDate);


//            String jsonResponse = fetchJsonResponse(apiUrl2);
//            JSONObject jsonObject4 = new JSONObject(jsonResponse);
//            WeatherData2 currentHourData = WeatherApiService.parseWeatherData(jsonObject4);


            WeatherData weatherData = new WeatherData();
            weatherData.setTemperature(convertCelsiusFromKelvin(temperature));
            weatherData.setWindSpeed(windSpeed + "");
            weatherData.setPressure(pressure + "");
            weatherData.setSunrise(formattedSunriseTime);
            weatherData.setSunset(formattedSunsetTime);
            Log.d("MyApp", "sunrise: " + formattedSunriseTime);
            weatherData.setStatus(description);
            weatherData.setHumidity(humidity + "");
            weatherData.setFeelsLike(convertCelsiusFromKelvin(feelsLike));
            weatherData.setId(id);

            return weatherData;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WeatherData weatherData) {
        super.onPostExecute(weatherData);

        if (callback != null && callback.get() != null) {
            callback.get().onDataFetched(weatherData);
        }
    }

    String convertCelsiusFromKelvin(double kelvin) {
        double temperatureCelsius = kelvin - 273.15;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(temperatureCelsius);
    }
}