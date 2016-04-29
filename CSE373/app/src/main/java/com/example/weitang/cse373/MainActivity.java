package com.example.weitang.cse373;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private LocationManager locationManager;
    private String provider;
    private TextView longtitude;
    private TextView latitude;
    private TextView timeView;
    private String key = "AIzaSyAlxxW4e09Knk0W1JrkvS8r_von5V6xOpw";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        longtitude = (TextView)findViewById(R.id.textView_long);
        latitude = (TextView)findViewById(R.id.textView_lat);
        timeView = (TextView)findViewById(R.id.textView_time);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria  = new Criteria();
        provider = locationManager.getBestProvider(criteria,false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitude.setText("Location not available");
            longtitude.setText("Location not available");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
    @Override
    public void onLocationChanged(Location location){
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        longtitude.setText(lng);
        latitude.setText(lat);

        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="https://maps.googleapis.com/maps/api/timezone/json?location=39.6034810,-119.6822510\n";
        String url = "https://maps.googleapis.com/maps/api/timezone/json?location="+lat+","+lng+"&timestamp=0&key=AIzaSyAlxxW4e09Knk0W1JrkvS8r_von5V6xOpw";
        //TO-DO: JSON request.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                @Override
                    public void onResponse(JSONObject response){
                        Calendar time = Calendar.getInstance(TimeZone.getTimeZone(response.optString("timeZoneId").toString()));
                        timeView.setText(time.getTime().toString());
                }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        queue.add(jsonObjectRequest);
    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
