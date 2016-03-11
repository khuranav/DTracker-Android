package com.mycompany.hometomap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RideHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        View rootView = inflater.inflate(R.layout.fragment_main, con)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        });
        String [] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40"
        };
//        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
//        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(this, R.layout.list_item_ride_history, weekForecast);
//        ListView listview = (ListView) findViewById(R.id.list_item_history_textview);
//       // listView.setAdapter(mForecastAdapter);
//        return rootView;
    }

}
