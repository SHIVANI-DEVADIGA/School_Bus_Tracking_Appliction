package com.example.bustrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText et_phone,et_pass,et_cp,et_name;
    Button bt_reg;
    String st_pass="";
    String st_conf="";
    String url_update_data="https://db.tinkerspace.in/Srinivas_mukka/locker_app/register_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_phone=findViewById(R.id.et_phone);
        et_name=findViewById(R.id.et_name);
        et_pass=findViewById(R.id.et_pass);
        et_cp=findViewById(R.id.et_cp);
        bt_reg=findViewById(R.id.bt_reg);

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st_pass=et_pass.getText().toString().trim();
                st_conf=et_pass.getText().toString().trim();

                if(st_pass.equals(st_conf)) {


                    try {
                        volleyUpdateData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(Register.this, "invalid password", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public void volleyUpdateData() throws
            JSONException
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_data,
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
                                Toast.makeText(Register.this, "success", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Register.this,Login.class);
                                startActivity(intent);
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
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", et_name.getText().toString().trim());
                params.put("phone", et_phone.getText().toString().trim());
                params.put("pw", et_pass.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
