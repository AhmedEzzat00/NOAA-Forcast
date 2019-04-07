package com.weather.archangel.noah;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * Inflating the Menu Option when the activity created
     *
     * @param menu menu to inflate
     * @return boolean value indicating the succession or falling of the inflation process
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * get the item id from the option menu and then do an operation
     * @param item MenuItem that is selected
     * @return boolean value indicating the succession or falling of the process
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
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

            //we need a refenrenc to the root view because we cannot gain access
            //to findViewById from static class
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            List<String> weekForecastList = new ArrayList<String>(Arrays.asList(weatherDummyData));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getActivity(), R.layout.list_item_forcast, R.id.list_item_forecast_textview, weekForecastList);
            ListView listView = rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);
            return rootView;

        }
    }

}
