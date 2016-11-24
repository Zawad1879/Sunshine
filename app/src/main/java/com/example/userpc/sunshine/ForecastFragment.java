
package com.example.userpc.sunshine;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;
    TextView x;
    TextView y;
    SharedPreferences.Editor editor;
    boolean showProgress=false;
    private ProgressDialog forecastProgress;
    private static double currentMax;
    private static double currentMin;
    private static double nextMax;
    private static double nextMin;
    Activity activity;
    String description;
    private String location;
    private String unitType;

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }


    public interface passMaxMin{
        public void minMax(double max, double min, String weatherDescription, String cityName, double nextMax, double nextMin);
    }

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        updateWeather();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {

        super.onStart();

        updateWeather();
    }

    public void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        Log.v("UPDATEWEATHER", "Location is " + location);
        //    SharedPreferences p = getActivity().getSharedPreferences(
        //           "com.example.app", Context.MODE_PRIVATE);
        editor = prefs.edit();
        weatherTask.execute(location);
     //   showProgress=true;
   //     forecastProgress = ProgressDialog.show(getActivity(), "",
   //             "Loading..", true);
//        long delayInMillis = 1000;
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                forecastProgress.dismiss();
//            }
//        }, delayInMillis);

     //  forecastProgress = ProgressDialog.show(getActivity(), "",
     //           "Loading..", true);
     //   Log.v("Forecast fragment","Progress dialog appearing");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                //        weekForecast);
                new ArrayList<String>());


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                //       Toast.makeText(getActivity(),"position "+position,Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
            super.onPreExecute();
        }

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        String loc = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));


        private String getReadableDateString(long time) {

            Date date = new Date(time * 1000);
            SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
            return format.format(date).toString();
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            unitType = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
            Log.i("formathighlow", unitType);
            if (unitType.equals(getString(R.string.pref_units_imperial))) {
                Log.d(LOG_TAG, "Converted");
                high = (high * 1.8) + 32;
                low = (low * 1.8) + 32;
            } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
                Log.d(LOG_TAG, "Unit Type not found: " + unitType);


            }

            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }


        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            Log.v(LOG_TAG, "The location in the JSON is " + loc);
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "main";
            final String OWM_MAX = "temp_max";
            final String OWM_MIN = "temp_min";
            final String OWM_DATETIME = "dt";
            final String OWM_DESCRIPTION = "main";
            final String OWM_DATETEXT = "dt_txt";
            final String OWM_HUMIDITY = "humidity";
            final String OWM_PRESSURE = "pressure";
            final String OWN_WINDSPEED = "speed";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            String cityname = (forecastJson.getJSONObject("city")).getString("name");
            Log.v("getWeatherDataFromJson", "The city name is " + cityname);
            if (!(cityname.equals(loc))) {
                ;
                return null;
            } else {
                JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);


                String[] resultStrs = new String[numDays];
                for (int i = 0; i < weatherArray.length(); i++) {
                    //using the format "Day, description, hi/low"
                    String day;

                    String highAndLow;
                    String dayAndTime;


                    JSONObject dayForecast = weatherArray.getJSONObject(i);

                    // The date/time is returned as a long.
                    long dateTime = dayForecast.getLong(OWM_DATETIME);
                    day = getReadableDateString(dateTime);


                    //The date and time are obtained as a text
                    dayAndTime = dayForecast.getString(OWM_DATETEXT);



                    JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                    description = weatherObject.getString(OWM_DESCRIPTION);


                    JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                    double high = temperatureObject.getDouble(OWM_MAX);
                    double low = temperatureObject.getDouble(OWM_MIN);

                    if (i == 0) {
                          currentMax=high;
                        currentMin=low;
                    }
                    if (i == 1) {
                        nextMax=high;
                        nextMin=low;
                    }
                    highAndLow = formatHighLows(high, low);

                    resultStrs[i] = dayAndTime + " - " + description + " - " + highAndLow;
                    saveArray(resultStrs, "resultStrs", getActivity());
                }

                for (String s : resultStrs) {
                    Log.v(LOG_TAG, "Forecast entry: " + s);
                }
                return resultStrs;

            }
        }

        @Override
        protected String[] doInBackground(String... params) {


            if (params.length == 0) {
                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 32;
            String appid = "d9611ff7b146b1606f712934f60f0648";


            try {

                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, appid)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                Toast.makeText(getActivity(), "City was not found", Toast.LENGTH_SHORT).show();

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonStr, numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {

                mForecastAdapter.clear();
                for (String dayForecastStr : result) {
                    mForecastAdapter.add(dayForecastStr);
                }


            } else {
                Toast.makeText(getActivity(), "City not found", Toast.LENGTH_SHORT).show();

            }
            Log.v("FOrecast Fragment", "Dismiss dialog");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try{
                Log.d("Forecast@#%%$#", loc);
                if (unitType.equals("Metric")) {
                    ((passMaxMin) activity).minMax(currentMax, currentMin, description, loc,nextMax ,nextMin );
                }
                else{
                    ((passMaxMin) activity).minMax((currentMax*1.8)+32, (currentMin*1.8)+32, description, loc,nextMax ,nextMin  );
                }
            }catch (ClassCastException cce){

            }
        }


        public boolean saveArray(String[] array, String arrayName, Context mContext) {
            SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(arrayName + "_size", array.length);
            for (int i = 0; i < array.length; i++)
                editor.putString(arrayName + "_" + i, array[i]);
            return editor.commit();
        }

        public String[] loadArray(String arrayName, Context mContext) {
            SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
            int size = prefs.getInt(arrayName + "_size", 0);
            String array[] = new String[size];
            for (int i = 0; i < size; i++)
                array[i] = prefs.getString(arrayName + "_" + i, null);
            return array;
        }
    }
}
