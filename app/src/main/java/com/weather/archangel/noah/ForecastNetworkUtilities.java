package com.weather.archangel.noah;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ForecastNetworkUtilities {
    private static final String LOG_TAG = ForecastNetworkUtilities.class.getName();

    /**
     * This Class is meant to hold static variables and methods.
     * so there is no need to create an instance of the ForecastNetworkUtilities
     */
    private ForecastNetworkUtilities() {
    }

    public static String[] fetchForecastData(String stringURL) throws JSONException {
        URL url = createURL(stringURL);
        String jsonResponse = "";
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getWeatherDataFromJson(jsonResponse, 7);
    }

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating URL ", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) return jsonResponse;

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //Adding Timeout Conditions
            urlConnection.setReadTimeout(3000);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in Making a Connection ");
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while reading from InputStream ", e);
            }
        }
        return stringBuilder.toString();
    }


    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */

    /**
     * Convert time in MilliSecond into data format of DayName Month Number
     *
     * @param time time millisecond
     * @return date to print
     */
    private static String getReadableDateString(long time) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private static String formatHighLows(double high, double low) {
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);
        return roundedHigh + "/" + roundedLow;
    }

    /**
     * Convert the time to the UTC time zone
     */
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        GregorianCalendar date = (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        date.setTime(new Date(startDate));
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //transform your calendar to a long in the way you prefer
        return date.getTimeInMillis();
    }

    /**
     * Construct the weather data from the json response of the api request
     */
    private static String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE_MAIN = "main";
        final String OWM_MAX = "temp_max";
        final String OWM_MIN = "temp_min";
        final String OWM_WEATHER_DESCRIPTION = "main";
        final String OWE_DATE_MILLISECONDS = "dt";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        String[] resultStrs = new String[numDays];
        for (int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long dateTime = dayForecast.getInt(OWE_DATE_MILLISECONDS);
            day = getReadableDateString(normalizeDate(dateTime));

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_WEATHER_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE_MAIN);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        for (String s : resultStrs) {
            Log.i(LOG_TAG, "Forecast entry: " + s);
        }
        return resultStrs;

    }
}
