package com.example.imad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue q= Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView)findViewById(R.id.tv1);
        Button bt1 = (Button)findViewById(R.id.bt1);
        String url = "https://api.ximilar.com/recognition/v2/classify";
//        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                tv.setText(response);
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                tv.setText(error.toString());
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap headers = new HashMap();
//                headers.put("Content-Type","application/json");
//                headers.put("Authorization","Token 6094fab5d0e260d1d39b7e9f0a77b8b3648f4826");
//                return headers;
//            }
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map params = new HashMap();
//                Map records = new HashMap();
//
//                params.put("task_id", "5b55ec61-b730-4dcf-b8d6-d2f30fabe705");
//
//                return params;
//            }
//        };
        //encode image to base64 string
        Intent i = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(i);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        JSONObject jsonParams = new JSONObject();

        try {
            //Add string params
            jsonParams.put("task_id", "5b55ec61-b730-4dcf-b8d6-d2f30fabe705");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Create json array for filter
        JSONArray array=new JSONArray();

        //Create json objects for two filter Ids
        JSONObject jsonParam1 =new JSONObject();


        try {

            jsonParam1.put("_base64",imageString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Add the filter Id object to array
        array.put(jsonParam1);


        //Add array to main json object
        try {
            jsonParams.put("records",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                tv.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","Token 6094fab5d0e260d1d39b7e9f0a77b8b3648f4826");
                return headers;
            }
        };





        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                q.add(jsonObjectRequest);
//                tv.setText(imageString);
            }
        });

    }


}