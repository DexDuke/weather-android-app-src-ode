package com.example.weather.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class Api {
    private static final String API_Key="N3QRW56TZDATGACLEPFKM6RFC";
    private static final String BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

    public  JsonNode callAPI(String latitude, String longitude) throws IOException {
        String apiUrl = BASE_URL + latitude + "," + longitude + "?unitGroup=us&key=" +API_Key;

        //create a URL with the current link(string)
        URL url=new URL(apiUrl);

        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode=connection.getResponseCode();

        if(responseCode==HttpURLConnection.HTTP_OK) {
            // Read and parse the response data
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();


            String jsonResponse = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            String description = jsonNode.get("description").asText();

            //get the current day's weather forecast:
            JsonNode days = jsonNode.get("days");

            return  days;
        }else {
            return  null;
        }

    }
}
