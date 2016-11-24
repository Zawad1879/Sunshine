package com.example.userpc.sunshine;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;



public class Barbecue extends ActionBarActivity {

    private double nextMax;
    private double nextMin;
    private String description;
    private TextView status;
    private TextView bbq_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbecue);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        status = (TextView) findViewById(R.id.activity_barbecue_status);
        bbq_desc = (TextView) findViewById(R.id.activity_barbecue_description);
        nextMax = bundle.getDouble("nextMax");
        nextMin = bundle.getDouble("nextMin");
        description = bundle.getString("description");

        if (((nextMax + nextMin) / 2) > 20 && description.equals("Clear")) {
            status.setText("Perfect!");

            bbq_desc.setText("It's a beautiful weather for a barbecue outside with your friends and family under the glorious sun");

        }
        else{
            status.setText("Maybe not today..");


        }
    }

}
