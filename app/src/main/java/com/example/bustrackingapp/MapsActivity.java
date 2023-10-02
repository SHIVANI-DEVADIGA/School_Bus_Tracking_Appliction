package com.example.bustrackingapp;

import static java.lang.Double.NaN;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.icu.util.IndianCalendar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.bustrackingapp.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.NullCipher;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    String url_get_location="https://db.tinkerspace.in/Srinivas_mukka/bus_tracking_app/get_current_location.php";

    Double lat = NaN;
    Double lng = NaN ;
    RequestQueue requestQueue;
    String jsonResponse="";
    Handler mHandler;
    Button bt_get;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        requestQueue = Volley.newRequestQueue(this);
        bt_get = findViewById(R.id.bt_get);
        this.mHandler = new Handler();
        m_Runnable.run();

        try {
            volleyGetStatus();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, MapHistory.class);
                startActivity(intent);

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Lat: "+lat, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Lng: "+lng, Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng anywhere = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions().position(anywhere).title("Hello Maps");
        mMap.addMarker(new MarkerOptions().position(anywhere).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(anywhere, 15));
    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()
        {
            try {
                volleyGetStatus();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MapsActivity.this.mHandler.postDelayed(m_Runnable,5000);
        }
    };

    public void volleyGetStatus() throws JSONException
    {
        JsonArrayRequest req = new JsonArrayRequest(url_get_location,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {


                        try {
                            jsonResponse = "";


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                lat = jsonObject.getDouble("Latitude");
                                lng = jsonObject.getDouble("Longitude");
                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);
                                mapFragment.getMapAsync(MapsActivity.this);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(req);
    }
    public void update_ui(){

        Toast.makeText(this, "Lat: "+lat, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Lng: "+lng, Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
        LatLng anywhere = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions().position(anywhere).title("Hello Maps");
        mMap.addMarker(new MarkerOptions().position(anywhere).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(anywhere, 15));
    }

}
