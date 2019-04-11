package com.weather.archangel.noah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import java.util.ArrayList;

public class ForecastFragment extends Fragment {
    ArrayAdapter<String> adapter = null;
    final String FORECAST_BASE_URL =
            "http://api.openweathermap.org/data/2.5/forecast?";
    private static final String LOG_TAG = ForecastFragment.class.getName();
    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //we need a reference to the root view because we cannot gain access
        //to findViewById from static class
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new ArrayAdapter<>
                (getActivity(), R.layout.list_item_forcast, R.id.list_item_forecast_textview, new ArrayList<String>());
        ListView listView = rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecastInfo = adapter.getItem(position);
                //Launch the details activity with the forecast info
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, forecastInfo);
                startActivity(intent);
                Toast.makeText(getActivity(), forecastInfo, Toast.LENGTH_SHORT).show();
            }
        });

        new AsyncWeather().execute(FORECAST_BASE_URL);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "onStart", Toast.LENGTH_SHORT).show();
        new AsyncWeather().execute(FORECAST_BASE_URL);
    }

    public class AsyncWeather extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... urls) {
            String urlToSearch = buildURLAttributes(urls[0]);
            String[] forecastData = new String[1];
            try {
                forecastData = ForecastNetworkUtilities.fetchForecastData(urlToSearch);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(LOG_TAG, String.valueOf(forecastData.length));
            return forecastData;
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            if (weatherData != null && weatherData.length != 0) {
                adapter.clear();
                adapter.addAll(weatherData);
                adapter.notifyDataSetChanged();
            }

        }
    }

    private String buildURLAttributes(String baseURL) {

        //placeholder data

        int numDays = 7;

        final String QUERY_PARAM = "q";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "appid";
        final String OPEN_WEATHER_MAP_API_KEY = "807f66eeba93d5b57e1f30dee477a57b";

        //Get and retrieve the data from XML/User
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        String temperature_unit = prefs.getString(getString(R.string.pref_temperature_key),
                getString(R.string.pref_temperature_celsius_value));

        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(APPID_PARAM, OPEN_WEATHER_MAP_API_KEY)
                .appendQueryParameter(UNITS_PARAM, temperature_unit)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        return builtUri.toString();
    }

}