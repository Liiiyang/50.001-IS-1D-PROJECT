package com.example.liyang.a1d;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowRoute extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap myMap;
    Polyline line;
    Context context;
    Marker marker;
    private static String[] name_id = new String[2];
    private FirebaseDatabase mref;
    private DatabaseReference rootRef;

    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_showroute);
        context = ShowRoute.this;
        // GoogleMap myMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myMap.setMyLocationEnabled(true);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        // TODO: Change Start Lat Lng to the coordinates set in the first activity
        LatLng startLatLng = new LatLng(MainActivity.latlng[0],MainActivity.latlng[1]);
        marker = myMap.addMarker(new MarkerOptions().position(startLatLng).title("Start Location: " + MainActivity.name[0]));
        LatLng endLatLng;
        String urlTopass;
        for(ParkingInfo info: displayResult.Pchosen){
            if(displayResult.ChosenPark[0].equals(info.getParkingname())){
                endLatLng = new LatLng(info.getLatitude(),info.getLongitude());
                marker = myMap.addMarker(new MarkerOptions().position(endLatLng).title("Destination: " +info.getParkingname()));
                urlTopass = makeURL(startLatLng.latitude,
                        startLatLng.longitude, endLatLng.latitude,
                        endLatLng.longitude);
                new connectAsyncTask(urlTopass).execute();
                name_id[0] = info.getId();
                name_id[1] = info.getParkingname();
            }
            if(displayResult.ChosenPark[0].equals(info.getParkingname())){
                endLatLng = new LatLng(info.getLatitude(),info.getLongitude());
                marker = myMap.addMarker(new MarkerOptions().position(endLatLng).title("Destination: " +info.getParkingname()));
                urlTopass = makeURL(startLatLng.latitude,
                        startLatLng.longitude, endLatLng.latitude,
                        endLatLng.longitude);
                new connectAsyncTask(urlTopass).execute();
                name_id[0] = info.getId();
                name_id[1] = info.getParkingname();
            }
            if(displayResult.ChosenPark[0].equals(info.getParkingname())){
                endLatLng = new LatLng(info.getLatitude(),info.getLongitude());
                marker = myMap.addMarker(new MarkerOptions().position(endLatLng).title("Destination: " +info.getParkingname()));
                urlTopass = makeURL(startLatLng.latitude,
                        startLatLng.longitude, endLatLng.latitude,
                        endLatLng.longitude);
                new connectAsyncTask(urlTopass).execute();
                name_id[0] = info.getId();
                name_id[1] = info.getParkingname();
            }
        }
        // Add a marker in Singapore and move the camera
        float zoomLevel = 14.0f;
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, zoomLevel));
        myMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
    }

    //Adds the Selected Route to a Favorite Recycler View
    public void savfav(View v){
        mref = FirebaseDatabase.getInstance();
        rootRef = mref.getReference();
        rootRef.child(name_id[0]).child("saved").setValue(1);
        Toast.makeText(getApplicationContext(), "Added to Favourites", Toast.LENGTH_LONG).show();
    }


    private class connectAsyncTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;
        String url;
        connectAsyncTask(String urlPass){
            url = urlPass;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(ShowRoute.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result!=null){
                drawPath(result);
            }
        }
    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        String url = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin="+Double.toString(sourcelat)+","+Double.toString(sourcelog) +
                "&destination="+Double.toString(destlat)+","+Double.toString(destlog) +
                "&sensor=false" +
                "&mode=walking" +
                "&alternatives=true";

        return url;
    }



    public void drawPath(String result) {
        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }
            line = myMap.addPolyline(options);
        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}