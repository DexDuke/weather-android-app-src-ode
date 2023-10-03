package com.example.weather.api;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;

public class FindLocation {
    public String getAddressFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        String city = "";
        String division = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                city = address.getLocality(); // Get the city name
                division = address.getAdminArea(); // Get the division name
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return city + ", " + division;
    }
}
