package com.example.userpc.sunshine;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;



public class Travel extends ActionBarActivity {


    private String description;
    private TextView status;
    private TextView trav_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        status = (TextView) findViewById(R.id.activity_travel_status);
        trav_desc = (TextView) findViewById(R.id.activity_travel_description);
        description = bundle.getString("description");

        if (description.equals("Clear")) {
            status.setText("Perfect!");
            trav_desc.setText("It's a beautiful weather to travel, be it on a plane or a long drive chasing the sun");

        }
        else{
            status.setText("Maybe not today..");


        }
    }

}
