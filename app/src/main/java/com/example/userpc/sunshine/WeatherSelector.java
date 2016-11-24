package com.example.userpc.sunshine;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.parse.ParseObject;


public class WeatherSelector extends ActionBarActivity {


    ParseObject nowcast = new ParseObject("Nowcast");
    String toSendToParse;
    EditText locationInput;
    ImageButton sunButton;
    ImageButton rainButton;
    ImageButton stormButton;
    ImageButton cloudButton;
    ImageButton mistButton;
    ImageButton snowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_selector);


        locationInput = (EditText) findViewById(R.id.locationInput);


        sunButton =(ImageButton)findViewById(R.id.sunButton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkNull = locationInput.getText().toString();
                if (checkNull.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                }
                else {
                    sun();
                }
            }
        });

        rainButton =(ImageButton)findViewById(R.id.rainButton);
        rainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkNull = locationInput.getText().toString();
                if (checkNull.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                }
                else {
                    rain();
                }
            }
        });

        stormButton =(ImageButton)findViewById(R.id.stormButton);
        stormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkNull = locationInput.getText().toString();
                if (checkNull.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                }
                else {
                    storm();
                }
            }
        });

        cloudButton =(ImageButton)findViewById(R.id.cloudButton);
        cloudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkNull = locationInput.getText().toString();
                if (checkNull.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                }
                else {
                    cloudy();
                }
            }
        });


        mistButton =(ImageButton)findViewById(R.id.mistButton);
        mistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkNull = locationInput.getText().toString();
                if (checkNull.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                }
                else {
                    mist();
                }
            }
        });


        snowButton =(ImageButton)findViewById(R.id.snowButton);
        snowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkNull = locationInput.getText().toString();
                if (checkNull.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                }
                else {
                    snow();
                }
            }
        });

    }

    public void storm() {

        toSendToParse = locationInput.getText().toString();
        if (toSendToParse.equals(null)){}
        nowcast.put("Location", toSendToParse);
        nowcast.put("Weather", "Stormy");
        nowcast.saveInBackground();
        Toast.makeText(getApplicationContext(),"Nowcast data saved!",Toast.LENGTH_LONG).show();
    }


    public void sun(){
        toSendToParse = locationInput.getText().toString();
        nowcast.put("Location", toSendToParse);
        nowcast.put("Weather", "Sunny");
        nowcast.saveInBackground();
        Toast.makeText(getApplicationContext(),"Nowcast data saved!",Toast.LENGTH_LONG).show();
    }

    public void cloudy() {

        toSendToParse = locationInput.getText().toString();
        nowcast.put("Location", toSendToParse);
        nowcast.put("Weather", "Cloudy");
        nowcast.saveInBackground();
        Toast.makeText(getApplicationContext(),"Nowcast data saved!",Toast.LENGTH_LONG).show();

    }

    public void rain() {

        toSendToParse = locationInput.getText().toString();
        nowcast.put("Location", toSendToParse);
        nowcast.put("Weather", "Rainy");
        nowcast.saveInBackground();
        Toast.makeText(getApplicationContext(),"Nowcast data saved!",Toast.LENGTH_LONG).show();

    }

    public void snow() {

        toSendToParse = locationInput.getText().toString();
        nowcast.put("Location", toSendToParse);
        nowcast.put("Weather", "Snowy");
        nowcast.saveInBackground();
        Toast.makeText(getApplicationContext(),"Nowcast data saved!",Toast.LENGTH_LONG).show();

    }

    public void mist() {

        toSendToParse = locationInput.getText().toString();
        nowcast.put("Location", toSendToParse);
        nowcast.put("Weather", "Misty");
        nowcast.saveInBackground();
        Toast.makeText(getApplicationContext(),"Nowcast data saved!",Toast.LENGTH_LONG).show();
    }



}
