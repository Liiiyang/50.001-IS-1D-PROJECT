package com.example.liyang.a1d;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends AppCompatActivity {
    int PLACE_PICKER_REQUEST = 1;
    Switch simpleswitch;
    Button go;
    Button find_button;
    private LatLng location;
    public static Double[] latlng = new Double[2];
    public static String[] name = new String[1];
    private CharSequence cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Select Current Location");
        go = (Button) findViewById(R.id.go);
        find_button = (Button) findViewById(R.id.find_button);
        go.setVisibility(View.INVISIBLE);
        simpleswitch = (Switch)findViewById(R.id.simpleSwitch);
        simpleswitch.setChecked(false);
        simpleswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    find_button.setVisibility(View.INVISIBLE);
                    go.setVisibility(View.VISIBLE);
                }
                else{
                    find_button.setVisibility(View.VISIBLE);
                    go.setVisibility(View.INVISIBLE);
                }

            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourites, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.addfavourites:
                Intent fav = new Intent(this, com.example.liyang.a1d.RecyclerRoute.class);
                startActivity(fav);
                return true;
        }
        return super.onOptionsItemSelected(item);}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                LatLngBounds chosenPlace = PlacePicker.getLatLngBounds(data);
                Intent intent = new Intent(this,displayResult.class);
                intent.putExtra("chosenPlace",chosenPlace);
                startActivity(intent);
                Place place = PlacePicker.getPlace(this,data);
                location = place.getLatLng();
                latlng[0] = location.latitude;
                latlng[1] = location.longitude;
                cname = place.getName();
                name[0] = cname.toString();


            }
        }

    }
    public void onFind(View v){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(new LatLngBounds(new LatLng(1.4262107,103.832162),new LatLng(1.4330602,103.8451675)));
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e){
            Toast.makeText(this,"Unable to launch placepicker",Toast.LENGTH_LONG).show();
        }
    }
    public void find(View v){
        Intent displayResult = new Intent(this, com.example.liyang.a1d.displayResult.class);
        startActivity(displayResult);
        latlng[0] = 1.4330602;
        latlng[1] = 103.8451675;
        name[0] = "Yishun Avenue 6";

    }
}