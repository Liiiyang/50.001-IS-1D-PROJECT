package com.example.liyang.a1d;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class displayResult extends AppCompatActivity{

    Button name1;
    TextView price1;
    TextView lot1;
    TextView distance1;
    Button name2;
    TextView price2;
    TextView lot2;
    TextView distance2;
    Button name3;
    TextView price3;
    TextView lot3;
    TextView distance3;
    TextView parkingcharges;
    private FirebaseDatabase mref;
    private DatabaseReference rootRef;
    HashMap<String,Float> CPPrice = new HashMap<>();
    HashMap<String,String> PriceSelection = new HashMap<>();
    HashMap<String,Float> CPDistance = new HashMap<>();
    public static HashMap<String,Integer> CPLots = new HashMap<>();
    ParkingInfo CPInfo;
    public static ArrayList<ParkingInfo> Pchosen = new ArrayList<>();
    float distance = 0;
    Calendar calendar;
    String selected_price = "";
    //public static ArrayList<String> ChosenPark = new ArrayList<>();
    public static String[] ChosenPark = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayresult);
        name1 = (Button) findViewById(R.id.name1);
        price1 = (TextView) findViewById(R.id.price1);
        lot1 = (TextView)findViewById(R.id.lot1);
        distance1 = (TextView) findViewById(R.id.distance1);
        name2 = (Button) findViewById(R.id.name2);
        price2 = (TextView) findViewById(R.id.price2);
        lot2 = (TextView)findViewById(R.id.lot2);
        distance2 = (TextView) findViewById(R.id.distance2);
        name3 = (Button) findViewById(R.id.name3);
        price3 = (TextView) findViewById(R.id.price3);
        lot3 = (TextView)findViewById(R.id.lot3);
        distance3 = (TextView) findViewById(R.id.distance3);
        parkingcharges = (TextView) findViewById(R.id.parkingcharges);
        mref = FirebaseDatabase.getInstance();
        rootRef = mref.getReference();
        rootRef.keepSynced(true);
        this.setTitle("Select Carpark");
        // TODO: Change Start Lat Lng to the coordinates set in the first activity
        final Location crntLocation=new Location("crntlocation");
        //crntLocation.setLatitude(1.4330602);
        //crntLocation.setLongitude(103.8451675);
        crntLocation.setLatitude(MainActivity.latlng[0]);
        crntLocation.setLongitude(MainActivity.latlng[1]);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ParkingInfo info = postSnapshot.getValue(ParkingInfo.class);
                    Location newLocation=new Location("newlocation");
                    newLocation.setLatitude(info.getLatitude());
                    newLocation.setLongitude(info.getLongitude());
                    priceselect();
                    //Calculates Distance from set location to the various carparks
                    distance = (crntLocation.distanceTo(newLocation))/1000;
                    //Adds values to firebase and to check that the distance and selected price are correct
                    rootRef.child(info.getId()).child("Distance").setValue(distance);
                    rootRef.child(info.getId()).child("Selected").setValue(selected_price);
                    CPInfo = new ParkingInfo(info.getId(),info.getLatitude(),info.getLongitude(),info.getLot(),info.getParkingname(),
                            info.getSaved(),info.getSunday1(),info.getSunday2(),info.getWeekday1(),info.getWeekday2());
                    Pchosen.add(CPInfo);
                    //Add distance to Hashmap for sorting
                    CPDistance.put(info.getId(),distance);
                    int lots = Integer.parseInt(info.getLot());
                    CPLots.put(info.getId(),lots);
                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        //Automatically updates table whenever parking lot number changes
        Query query = rootRef.orderByChild("location");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ParkingInfo info = dataSnapshot.getValue(ParkingInfo.class);
                CPInfo = new ParkingInfo(info.getId(),info.getLatitude(),info.getLongitude(),info.getLot(),info.getParkingname(),
                        info.getSaved(),info.getSunday1(),info.getSunday2(),info.getWeekday1(),info.getWeekday2());
                Pchosen.add(CPInfo);
                //Add distance to Hashmap for sorting
                CPDistance.put(info.getId(), distance);
                int lots = Integer.parseInt(info.getLot());
                CPLots.put(info.getId(), lots);
                Object firstKey = CPLots.keySet().toArray()[CPLots.size()-1];
                Object secondKey = CPLots.keySet().toArray()[CPLots.size()-2];
                Object thirdKey = CPLots.keySet().toArray()[CPLots.size()-3];
                for(ParkingInfo info_updates: Pchosen){
                    if(info_updates.getId() == firstKey){
                        name1.setText(info_updates.getParkingname());
                        distance1.setText(String.format("%.2f",CPDistance.get(firstKey))+"Km");
                        lot1.setText(info_updates.getLot());
                        price1.setText(PriceSelection.get(firstKey));

                    }
                    if(info_updates.getId() == secondKey){
                        name2.setText(info_updates.getParkingname());
                        distance2.setText(String.format("%.2f",CPDistance.get(secondKey))+"Km");
                        lot2.setText(info_updates.getLot());
                        price2.setText(PriceSelection.get(secondKey));
                    }
                    if(info_updates.getId() == thirdKey){
                        name3.setText(info_updates.getParkingname());
                        distance3.setText(String.format("%.2f",CPDistance.get(thirdKey))+"Km");
                        lot3.setText(info_updates.getLot());
                        price3.setText(PriceSelection.get(thirdKey));
                    }
                }

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        displayinfo();

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

    private void displayinfo() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.pricesort:
                        Object firstKey_price = sortMapByValue(CPPrice).keySet().toArray()[0];
                        Object secondKey_price = sortMapByValue(CPPrice).keySet().toArray()[1];
                        Object thirdKey_price = sortMapByValue(CPPrice).keySet().toArray()[2];
                        for(ParkingInfo info: Pchosen){
                            if(info.getId() == firstKey_price){
                                name1.setText(info.getParkingname());
                                distance1.setText(String.format("%.2f",CPDistance.get(firstKey_price))+"Km");
                                lot1.setText(info.getLot());
                                price1.setText(PriceSelection.get(firstKey_price));
                            }
                            if(info.getId() == secondKey_price){
                                name2.setText(info.getParkingname());
                                distance2.setText(String.format("%.2f",CPDistance.get(secondKey_price))+"Km");
                                lot2.setText(info.getLot());
                                price2.setText(PriceSelection.get(secondKey_price));
                            }
                            if(info.getId() == thirdKey_price){
                                name3.setText(info.getParkingname());
                                distance3.setText(String.format("%.2f",CPDistance.get(thirdKey_price))+"Km");
                                lot3.setText(info.getLot());
                                price3.setText(PriceSelection.get(thirdKey_price));
                            }

                        }
                        break;
                    case R.id.distancesort:
                        // Get first three values in the hashmap and display it in the table
                        Object firstKey = sortMapByValue(CPDistance).keySet().toArray()[0];
                        Object secondKey = sortMapByValue(CPDistance).keySet().toArray()[1];
                        Object thirdKey = sortMapByValue(CPDistance).keySet().toArray()[2];
                        for(ParkingInfo info: Pchosen){
                            if(info.getId() == firstKey){
                                name1.setText(info.getParkingname());
                                distance1.setText(String.format("%.2f",CPDistance.get(firstKey))+"Km");
                                lot1.setText(info.getLot());
                                price1.setText(PriceSelection.get(firstKey));
                            }
                            if(info.getId() == secondKey){
                                name2.setText(info.getParkingname());
                                distance2.setText(String.format("%.2f",CPDistance.get(secondKey))+"Km");
                                lot2.setText(info.getLot());
                                price2.setText(PriceSelection.get(secondKey));
                            }
                            if(info.getId() == thirdKey){
                                name3.setText(info.getParkingname());
                                distance3.setText(String.format("%.2f",CPDistance.get(thirdKey))+"Km");
                                lot3.setText(info.getLot());
                                price3.setText(PriceSelection.get(thirdKey));
                            }
                        }
                        break;
                    case R.id.lotsort:
                        //First Three keys in the hashmap will be displayed
                        Object firstKey_lot = sortMapByValue(CPLots).keySet().toArray()[CPLots.size()-1];
                        Object secondKey_lot = sortMapByValue(CPLots).keySet().toArray()[CPLots.size()-2];
                        Object thirdKey_lot = sortMapByValue(CPLots).keySet().toArray()[CPLots.size()-3];
                        for(ParkingInfo info: Pchosen){
                            if(info.getId() == firstKey_lot){
                                name1.setText(info.getParkingname());
                                distance1.setText(String.format("%.2f",CPDistance.get(firstKey_lot))+"Km");
                                lot1.setText(info.getLot());
                                price1.setText(PriceSelection.get(thirdKey_lot));
                            }
                            if(info.getId() == secondKey_lot){
                                name2.setText(info.getParkingname());
                                distance2.setText(String.format("%.2f",CPDistance.get(secondKey_lot))+"Km");
                                lot2.setText(info.getLot());
                                price2.setText(PriceSelection.get(secondKey_lot));
                            }
                            if(info.getId() == thirdKey_lot){
                                name3.setText(info.getParkingname());
                                distance3.setText(String.format("%.2f",CPDistance.get(thirdKey_lot))+"Km");
                                lot3.setText(info.getLot());
                                price1.setText(PriceSelection.get(firstKey_lot));
                            }
                        }
                        break;
                }
            }
        });
    }
    //Show the route to Car Park 1 when clicked
    public void carpark1(View view){
        for(ParkingInfo info: Pchosen){
            if(name1.getText().toString().equals(info.getParkingname())){
                //ChosenPark.add(info.getParkingname());
                ChosenPark[0] = info.getParkingname();
                break;
            }
        }
        Intent ShowRoute = new Intent(this, com.example.liyang.a1d.ShowRoute.class);
        startActivity(ShowRoute);
    }
    //Show the route to Car Park 2 when clicked
    public void carpark2(View view){
        for(ParkingInfo info: Pchosen){
            if(name2.getText().toString().equals(info.getParkingname())){
                //ChosenPark.add(info.getParkingname());
                ChosenPark[0] = info.getParkingname();
                break;
            }
        }
        Intent ShowRoute = new Intent(this, com.example.liyang.a1d.ShowRoute.class);
        startActivity(ShowRoute);
    }

    //Show the route to Car Park 3 when clicked
    public void carpark3(View view){
        for(ParkingInfo info: Pchosen){
            if(name3.getText().toString().equals(info.getParkingname())){
                //ChosenPark.add(info.getParkingname());
                ChosenPark[0] = info.getParkingname();
                break;
            }
        }
        Intent ShowRoute = new Intent(this, com.example.liyang.a1d.ShowRoute.class);
        startActivity(ShowRoute);
    }



    private Float priceregex(String price){
        float pricef = 0;
        String remove = price.replace("$","").replace("\n","");
        Pattern p = Pattern.compile("^[0-9].[0-9][0-9]([0-9][0-9])?");
        Matcher calprice = p.matcher(remove);
        while(calprice.find()){
            pricef = Float.parseFloat(calprice.group());
        }
        return pricef;

    }

    private void priceselect(){
        //Release Correct Pricing based on Day and Time
        calendar = Calendar.getInstance(TimeZone.getDefault());
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEE");
        Date date = new Date();
        String dayName = sdf_.format(date);

        for(ParkingInfo info:Pchosen){
            if (dayName.equals("Sunday")) {
                if (timeOfDay >= 7 && timeOfDay <=22) {
                    selected_price = info.getSunday1();
                    CPPrice.put(info.getId(),priceregex(selected_price));
                    PriceSelection.put(info.getId(),selected_price);
                    parkingcharges.setText("Current Parking Charges: DAY");
                    parkingcharges.setTextColor(Color.rgb(255,140,0));

                } else {
                    selected_price = info.getSunday2();
                    CPPrice.put(info.getId(),priceregex(selected_price));
                    PriceSelection.put(info.getId(),selected_price);
                    parkingcharges.setText("Current Parking Charges: NIGHT");
                    parkingcharges.setTextColor(Color.BLUE);
                }
            } else {
                if (timeOfDay >= 7 && timeOfDay <=22) {
                    selected_price = info.getWeekday1();
                    CPPrice.put(info.getId(),priceregex(selected_price));
                    PriceSelection.put(info.getId(),selected_price);
                    parkingcharges.setText("Current Parking Charges: DAY");
                    parkingcharges.setTextColor(Color.rgb(255,140,0));
                } else {
                    selected_price = info.getWeekday2();
                    CPPrice.put(info.getId(),priceregex(selected_price));
                    PriceSelection.put(info.getId(),selected_price);
                    parkingcharges.setText("Current Parking Charges: NIGHT");
                    parkingcharges.setTextColor(Color.BLUE);
                }
            }
        }
    }
    //Sort Hashmap Value
    public static <K, V extends Comparable<V>> LinkedHashMap<K, V> sortMapByValue(
            Map<K, V> map) {
        List<Map.Entry<K, V>> sortedEntries = sortEntriesByValue(map.entrySet());
        LinkedHashMap<K, V> sortedMap = new LinkedHashMap<K,V>(map.size());
        for (Map.Entry<K, V> entry : sortedEntries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private static <K, V extends Comparable<V>> List<Map.Entry<K, V>> sortEntriesByValue(
            Set<Map.Entry<K, V>> entries) {
        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(entries);
        Collections.sort(sortedEntries, new ValueComparator<V>());
        return sortedEntries;
    }

    private static class ValueComparator<V extends Comparable<V>> implements
            Comparator<Map.Entry<?, V>> {
        public int compare(Map.Entry<?, V> entry1, Map.Entry<?, V> entry2) {
            return entry1.getValue().compareTo(entry2.getValue());
        }
    }

}