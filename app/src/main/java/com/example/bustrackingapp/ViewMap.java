package com.example.bustrackingapp;

import static java.lang.Double.NaN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.bustrackingapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

public class ViewMap extends FragmentActivity implements OnMapReadyCallback {
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
        setContentView(R.layout.activity_view_map);
        requestQueue = Volley.newRequestQueue(this);
        bt_get = findViewById(R.id.bt_get);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        lat = Double.valueOf(b.getString("lat","12.2344"));
        lng = Double.valueOf(b.getString("lng","42.2344"));
        this.mHandler = new Handler();





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

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
}
