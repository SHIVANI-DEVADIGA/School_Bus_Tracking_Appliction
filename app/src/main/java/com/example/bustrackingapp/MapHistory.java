package com.example.bustrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import static java.lang.Double.NaN;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bustrackingapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapHistory extends FragmentActivity implements OnMapReadyCallback {
    ArrayList<String> list_date = new ArrayList<>();
    ArrayList<String> list_time = new ArrayList<>();
    ArrayList<String> list_bus_id = new ArrayList<>();
    ArrayList<String> list_comment = new ArrayList<>();
    ArrayList<String> list_lat = new ArrayList<>();
    ArrayList<String> list_lng = new ArrayList<>();
    Double lat = NaN;
    Double lng = NaN ;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    RecyclerView rv_map;
    RequestQueue requestQueue;
    MyAdapter myAdapter;
    String jsonResponse="";
    String url_get_history="https://db.tinkerspace.in/Srinivas_mukka/bus_tracking_app/get_location_history.php";
    Handler mHandler;
    Button bt_vl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_history);
        rv_map=findViewById(R.id.rv_map);
        requestQueue = Volley.newRequestQueue(this);
        bt_vl = findViewById(R.id.bt_vl);


        try {
            volleyGetHistory();
        }catch (JSONException e){
            e.printStackTrace();
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_map.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Lat: "+lat, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Lng: "+lng, Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng india = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions().position(india).title("Hello Maps");
        mMap.addMarker(new MarkerOptions().position(india).title("Marker in India"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(india, 15));
    }


    public void volleyGetHistory() throws JSONException
    {
        JsonArrayRequest req = new JsonArrayRequest(url_get_history,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray)
                    {
                        try {
                            jsonResponse = "";

                            list_date.clear();
                            list_time.clear();
                            list_comment.clear();
                            list_bus_id.clear();
                            list_lat.clear();
                            list_lng.clear();


                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                list_date.add(jsonObject.getString("Date"));
                                list_time.add(jsonObject.getString("time"));
                                list_comment.add(jsonObject.getString("Comment"));
                                list_bus_id.add(jsonObject.getString("bus_id"));
                                list_lat.add(jsonObject.getString("Latitude"));
                                list_lng.add(jsonObject.getString("Longitude"));
                            }

                            myAdapter = new MyAdapter();
                            rv_map.setAdapter(myAdapter);

                        } catch (JSONException e)
                        {
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
    public class MyAdapter extends RecyclerView.Adapter<MapHistory.MyAdapter.MyViewHolder>{
        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView tv_date,tv_time,tv_comment,tv_bus_id;
            Button bt_vl;

            public MyViewHolder(View itemView){
                super(itemView);
                tv_date=itemView.findViewById(R.id.tv_date);
                tv_time=itemView.findViewById(R.id.tv_time);
                tv_comment=itemView.findViewById(R.id.tv_cm);
                tv_bus_id=itemView.findViewById(R.id.tv_map);
                bt_vl = itemView.findViewById(R.id.bt_vl);
            }
        }
        @Override
        public MapHistory.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View v = getLayoutInflater().inflate(R.layout.row,parent,false);

            MapHistory.MyAdapter.MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        public void onBindViewHolder(MyAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position)
        {
            holder.tv_date.setText("Date:"+list_date.get(position));
            holder.tv_time.setText("time:"+list_time.get(position));
            holder.tv_comment.setText("Comment:"+list_comment.get(position));
            holder.tv_bus_id.setText("bus_id:"+list_bus_id.get(position));
            holder.bt_vl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lat = Double.valueOf(list_lat.get(position));
                    lng = Double.valueOf(list_lng.get(position));
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapHistory.this);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list_date.size();
        }

        ;
    }

}

