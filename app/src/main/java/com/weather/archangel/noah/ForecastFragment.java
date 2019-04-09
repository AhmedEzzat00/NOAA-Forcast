package com.weather.archangel.noah;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {

    private static final String LOG_TAG = ForecastFragment.class.getName();
    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] weatherDummyData = {
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };

        //we need a reference to the root view because we cannot gain access
        //to findViewById from static class
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        List<String> weekForecastList = new ArrayList<>(Arrays.asList(weatherDummyData));
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getActivity(), R.layout.list_item_forcast, R.id.list_item_forecast_textview, weekForecastList);
        ListView listView = rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        final String FORECAST_BASE_URL =
                "http://api.openweathermap.org/data/2.5/forecast?";
        String urlToSearch = buildURLAttributes(FORECAST_BASE_URL);
        new AsyncWeather().execute(urlToSearch);
        return rootView;
    }

    private String buildURLAttributes(String baseURL) {

        //placeholder data
        String format = "json";
        String units = "metric";
        int numDays = 7;
        int zipCode = 90430;


        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "appid";
        final String OPEN_WEATHER_MAP_API_KEY = "807f66eeba93d5b57e1f30dee477a57b";

        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, Integer.toString(zipCode))
                .appendQueryParameter(APPID_PARAM, OPEN_WEATHER_MAP_API_KEY)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        return builtUri.toString();
    }

    public static class AsyncWeather extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... urls) {
            String[] forecastData = new String[1];
            // Log.e("TESTTT",forecastData[0]);
            try {
                forecastData = ForecastNetworkUtilities.fetchForecastData(urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(LOG_TAG, String.valueOf(forecastData.length));
            return null;
        }
    }
}