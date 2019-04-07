package com.weather.archangel.noah;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";
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
        return rootView;

    }

    public class AsyncWeather extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            String jsonRespons = ForecastNetworkUtilities.fetchForecastData(urls[0]);
            return null;
        }
    }
}