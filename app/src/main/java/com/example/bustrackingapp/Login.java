package com.example.bustrackingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Login extends AppCompatActivity {
    EditText et_phone,et_pass;
    Button bt_log,bt_nr;
    String url_get_data ="https://db.tinkerspace.in/Srinivas_mukka/bus_tracking_app/login.php";
    RequestQueue requestQueue;
    String st_phone="";
    String st_pass="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);

        et_pass=findViewById(R.id.et_pass);
        et_phone=findViewById(R.id.et_phone);
        bt_log=findViewById(R.id.bt_log);
        bt_nr=findViewById(R.id.bt_nr);



        bt_nr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });


        bt_log.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                st_phone = et_phone.getText().toString().trim();
                st_pass = et_pass.getText().toString().trim();

                if (st_phone.length() != 10) {
                    et_phone.setError("Invalid phone number");
                } else if (st_pass.length() < 4) {
                    et_pass.setError("password too short");
                } else {

                    try {
                        volleyUpdateData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void volleyUpdateData() throws
            JSONException
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_get_data,
                new Response.Listener<String>()
                {
                    JSONObject res = null;
                    @Override
                    public void onResponse(String ServerResponse)
                    {
                        try {
                            res = new JSONObject(ServerResponse);
                            //Toast.makeText(getApplicationContext(), ServerResponse, Toast.LENGTH_SHORT).show();

                            if(res.getString("error").length() == 0)
                            {
                                Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Login.this,MapsActivity.class);
                                startActivity(intent);
                                finish();

                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        Toast.makeText(getApplicationContext(), "Error! ", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("phone",st_phone);
                params.put("pw",st_pass);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
