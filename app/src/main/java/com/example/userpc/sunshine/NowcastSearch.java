package com.example.userpc.sunshine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class NowcastSearch extends ActionBarActivity {

    String searchTextToString="";
    TextView showWeather;
    EditText searchText;
    String weatherData;
    int flagSunny=0;
    int flagRainy=0;
    int flagCloudy=0;
    int flagStormy=0;
    int flagMisty=0;
    int flagSnowy=0;
    String maxWeather="";


    String TAG="com.example.userpc.sunshine";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowcast_search);
//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, "3wUqAX6D1QNWv3yNCuBfR35nN0CVw5H9Y6uVeMQ7", "8hgaUN8HpVQnlwR1RVOqLUTvfxb1XzE9RSXMlSYo");
        dialog = new ProgressDialog(this);
        searchText=(EditText)findViewById(R.id.searchText);
        showWeather=(TextView)findViewById(R.id.showWeather);

    }

    public void searchLocationClick(View view){


        dialog.setMessage("Please wait");
        dialog.show();

  //      dialog.dismiss();
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                dialog.dismiss();
////whatever you want just you have to launch overhere.
//
//
//            }
//        }, 3000);
        getData();
//        dialog.dismiss();
//        Intent i=new Intent(this, MainActivity.class);
//        i.putExtra("weatherdb", weatherData);
//        startActivity(i);


     //   dialog.dismiss();


    }



    public void getData() {
        searchTextToString = searchText.getText().toString();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Nowcast");
        query.whereEqualTo("Location", searchTextToString);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) {

                    for (ParseObject weatherObject : object) {

                        weatherData = weatherObject.getString("Weather");
                        findMax();
                    }

                    if (maxWeather.equals(null) || maxWeather.equals("")) {
                        showWeather.setText(" ");
                        Toast.makeText(NowcastSearch.this, "No such location found", Toast.LENGTH_LONG).show();
                    } else {
                        showWeather.setText("The weather in " + searchTextToString + " is " + maxWeather);
                        maxWeather = "";
                        searchTextToString = "";
                    }

                } else {
//                    Log.d("NowcastSearch", e.getMessage());
                    showWeather.setText(" ");
//                    Log.i(TAG,"Weather for "+searchTextToString+" not found");
                    Toast.makeText(NowcastSearch.this, "No such location found", Toast.LENGTH_LONG).show();

                }
            }
        });
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
//whatever you want just you have to launch overhere.


            }
        }, 1000);
    }

    public void findMax(){

        //Log.i(TAG,""+weatherData);

        if (weatherData.equals("Sunny")){
            flagSunny=flagSunny+1;
        }
        if (weatherData.equals("Rainy")){
            flagRainy=flagRainy+1;
        }
        if (weatherData.equals("Cloudy")){
            flagCloudy=flagCloudy+1;
        }
        if (weatherData.equals("Stormy")){
            flagStormy=flagStormy+1;
        }
        if (weatherData.equals("Misty")){
            flagMisty=flagMisty+1;
        }
        if (weatherData.equals("Snowy")){
            flagSnowy=flagSnowy+1;
        }

        int max=0;
        maxWeather="Sunny";

        if (flagSunny>max){
            max=flagSunny;
            maxWeather="Sunny";
        }
        if (flagRainy>max){
            max=flagRainy;
            maxWeather="Rainy";
        }
        if (flagCloudy>max){
            max=flagCloudy;
            maxWeather="Cloudy";
        }
        if (flagStormy>max){
            max=flagStormy;
            maxWeather="Stormy";
        }
        if (flagMisty>max){
            max=flagMisty;
            maxWeather="Misty";
        }
        if (flagSnowy>max){
            max=flagSnowy;
            maxWeather="Snowy";
        }

    }



}
