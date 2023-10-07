package com.example.weather.Location;

import android.os.AsyncTask;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebsiteScrapingTask extends AsyncTask<Void, Void, List<String>> {
    private TextView localCity;
    private TextView division;

    public WebsiteScrapingTask(TextView localCity, TextView division) {
        this.localCity = localCity;
        this.division = division;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> strings = new ArrayList<>();
        try {
            // URL of the website to scrape
            String url = "https://mylocationnow.io/";

            // Connect to the website and fetch the HTML content
            Document doc = Jsoup.connect(url).get();

            // Select the elements with the specified class names
            Elements gridBoxHalfElements = doc.select(".grid-box-half");

            // Iterate through the selected elements
            for (Element gridBoxHalf : gridBoxHalfElements) {
                // Find the elements for city, country, and region within each grid-box-half
                Element cityElement = gridBoxHalf.select(".display__box--half-label:contains(City) + .display__box--half-value").first();
                Element countryElement = gridBoxHalf.select(".display__box--half-label:contains(Country) + .display__box--half-value").first();
                Element regionElement = gridBoxHalf.select(".display__box--half-label:contains(Region) + .display__box--half-value").first();

                // Extract the text from the elements
                String city = cityElement != null ? cityElement.text() : "";
                String country = countryElement != null ? countryElement.text() : "";
                String region = regionElement != null ? regionElement.text() : "";

                if (!city.isEmpty() && !country.isEmpty() && !region.isEmpty()) {
                    strings.add(region);
                    strings.add(city);
                    strings.add(country);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        if (strings.size() >= 2) {
            localCity.setText(strings.get(0)); // Index 1 corresponds to the city
            division.setText(strings.get(1));   // Index 0 corresponds to the region
        }
    }
}
