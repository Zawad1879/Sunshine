package com.example.userpc.sunshine;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class Lifestyle extends ListActivity {

    String[] itemname ={
            "Barbecue",
            "Travel",

    };
    private double nextMax;
    private double nextMin;
    private String description;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(position==0){
           Intent i = new Intent(Lifestyle.this, Barbecue.class);
            i.putExtra("nextMax",nextMax);
            i.putExtra("nextMin", nextMin);
            i.putExtra("description",description);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
        if(position==1){
            Intent i = new Intent(Lifestyle.this, Travel.class);
            i.putExtra("description",description);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }


        super.onListItemClick(l, v, position, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle);

        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            return;
        }
        nextMax = bundle.getDouble("nextMax");
        nextMin = bundle.getDouble("nextMin");
        description = bundle.getString("description");
        //Log.d("Lifestyle*&^&*(", nextMax + " " + nextMin);
//        this.setListAdapter(new ArrayAdapter<String>(
//                this, R.layout.lifestyle_item,
//                R.id.Itemname,itemname));
        setListAdapter(new MobileArrayAdapter(this, itemname));





    }
}