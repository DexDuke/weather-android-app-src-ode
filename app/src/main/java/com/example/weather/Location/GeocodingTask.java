package com.example.weather.Location;

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

public class GeocodingTask extends AsyncTask<Double, Void, String[]> {

    private static final String TAG = GeocodingTask.class.getSimpleName();

    private OnGeocodingCompleteListener listener;

    public GeocodingTask(OnGeocodingCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected String[] doInBackground(Double... params) {
        double latitude = params[0];
        double longitude = params[1];

        try {
            URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" +
                    latitude + "&lon=" + longitude);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } else {
                Log.e(TAG, "HTTP error code: " + responseCode);
                return null;
            }

            JSONObject jsonObject = new JSONObject(response.toString());
            String city = jsonObject.optString("address_city");
            String country = jsonObject.optString("address_country");

            return new String[]{city, country};
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onGeocodingComplete(result);
        }
    }

    public interface OnGeocodingCompleteListener {
        void onGeocodingComplete(String[] result);
    }
}
