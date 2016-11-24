package com.example.userpc.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements ForecastFragment.passMaxMin {

    String TAG = "com.example.userpc.sunshine";
    private String[] navigationsitems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle drawerListener;
    private TextView maxText;
    private TextView minText;
    private TextView cityText;
    private ImageView weatherImage;
    private double nextMax;
    private double nextMin;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    getSupportActionBar().setIcon(R.drawable.solona);
        setContentView(R.layout.activity_main);
        //     setTitle("Solona");
        //     maxText=(TextView)findViewById(R.id.activity_main_maxTemp);
        //     minText=(TextView)findViewById(R.id.activity_main_minTemp);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        maxText = (TextView) findViewById(R.id.activity_main_maxTemp);
        minText = (TextView) findViewById(R.id.activity_main_minTemp);
        minText = (TextView) findViewById(R.id.activity_main_minTemp);
        cityText = (TextView) findViewById(R.id.main_activity_cityName);
        weatherImage=(ImageView) findViewById(R.id.main_activity_image);
        navigationsitems = getResources().getStringArray(R.array.draweritems);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navigationsitems));
        drawerListener = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (drawerListener.onOptionsItemSelected(item)) {
                    return true;
                }
                return super.onOptionsItemSelected(item);
            }
        };
        mDrawerLayout.setDrawerListener(drawerListener);


        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        //   if (savedInstanceState == null) {
        //       getSupportFragmentManager().beginTransaction()
        //                 .add(R.id.container, new ForecastFragment())
        //                 .commit();
        //    }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void minMax(double max, double min, String weatherDescription, String cityName, double nextMa, double nextMi) {
        nextMax=nextMa;
        nextMin=nextMi;
        description=weatherDescription;

        maxText.setText("" + max);
        minText.setText("" + min);
        Log.d("MainActivity",cityName);
        cityText.setText(cityName);
//        weatherDescription=weatherDescription.toLowerCase();
        Log.d("Mainactivity image",weatherDescription);
        if(weatherDescription.equals("Clear")) {weatherImage.setImageResource(R.drawable.sunnyy);}
        else if(weatherDescription.equals("Clouds")) {weatherImage.setImageResource(R.drawable.cloudy);}
        else if(weatherDescription.equals("Rain")) {weatherImage.setImageResource(R.drawable.rainy);
            Log.d("Mainactivity image", "image set");}
        else if(weatherDescription.equals("Snow")) {weatherImage.setImageResource(R.drawable.snowy);}
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment myFragment = null;
            Intent i;
            switch (position) {
                case 0:
                    myFragment = new ForecastFragment();
                    // forecastProgress = ProgressDialog.show(MainActivity.this, "",
                    //          "Loading..", true);
                    break;
                case 1:


                    i = new Intent(MainActivity.this, Lifestyle.class);
                    i.putExtra("nextMax",nextMax);
                    i.putExtra("nextMin",nextMin);
                    i.putExtra("description", description);
                    startActivity(i);
                    break;

                case 2:
                    i = new Intent(MainActivity.this, NowcastSearch.class);
                    startActivity(i);
                    break;

                case 3:
                    i = new Intent(MainActivity.this, WeatherSelector.class);
                    startActivity(i);
                    break;


            }
            if (position == 0) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, myFragment).commit();

                //forecastProgress.dismiss();
            }


            mDrawerList.setItemChecked(position, true);
            getSupportActionBar().setTitle(navigationsitems[position]);
            mDrawerLayout.closeDrawers();
            //  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences();


        }
    }


}