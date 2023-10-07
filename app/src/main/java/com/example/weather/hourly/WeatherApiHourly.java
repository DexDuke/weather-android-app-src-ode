package com.example.weather.hourly;

import android.os.AsyncTask;

import com.example.weather.hourly.WeatherDataHourly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApiHourly {

    private static final String API_KEY = "108e658ece27b123c22273339290aec2";

    public interface WeatherResponseListener {
        void onWeatherDataReceived(WeatherDataHourly weatherData);
    }

    public static void getHourlyWeather(final double latitude, final double longitude, final WeatherResponseListener listener) {
        new AsyncTask<Void, Void, WeatherDataHourly>() {
            @Override
            protected WeatherDataHourly doInBackground(Void... voids) {
                try {
                    String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY;
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

                    JSONObject data = new JSONObject(response.toString());
                    JSONArray list = data.getJSONArray("list");

                    // Assuming the list contains weather data for every 3 hours.
                    JSONObject currentHourData = list.getJSONObject(0);
                    JSONObject nextHourData = list.getJSONObject(1);
                    JSONObject previousHourData = list.getJSONObject(2);

                    double currentTemp = currentHourData.getJSONObject("main").getDouble("temp");
                    double nextHourTemp = nextHourData.getJSONObject("main").getDouble("temp");
                    double previousHourTemp = previousHourData.getJSONObject("main").getDouble("temp");
                    double currentWindSpeed = currentHourData.getJSONObject("wind").getDouble("speed");
                    // You can fetch other weather details like wind speed here

                    return new WeatherDataHourly(currentTemp, nextHourTemp, previousHourTemp, currentWindSpeed);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(WeatherDataHourly result) {
                if (result != null && listener != null) {
                    listener.onWeatherDataReceived(result);
                }
            }
        }.execute();
    }

}
