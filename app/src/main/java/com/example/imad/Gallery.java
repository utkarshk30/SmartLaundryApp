package com.example.imad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gallery extends AppCompatActivity {
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    List<Bitmap> l = new ArrayList<Bitmap>();
    Map<Integer,Integer> track;
    Map<String, JSONObject> result;
    RequestQueue q;
    int i;
    int imgViews;
    int k;
    int j,err;

    AlertDialog.Builder builder;
    JSONObject fn;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    AlertDialog diag,diag2;
    ImageView display,del;
    int curr;
    String imageEncoded;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mArrayUri = new ArrayList<Uri>();
        q = Volley.newRequestQueue(Gallery.this);
        i = 0;
        j=0;
        err=0;
        k=0;
        result = new HashMap<String,JSONObject>();
        track = new HashMap<Integer,Integer>();
        imgViews=0;
        // showing all images in imageswitcher
//        imageView.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                ImageView imageView1 = new ImageView(getApplicationContext());
//                return imageView1;
//            }
//        });
        Intent intent = new Intent();

        // setting type to select to be image
        intent.setType("image/*");

        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        processImages();

    }
    public void processImages(){
        for(Uri u:mArrayUri) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);

                processBitmap(bitmap,k);
                k++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        progressBar = new ProgressDialog(Gallery.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Requests Processing...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(l.size());
        progressBar.show();
        progressBarStatus = i;


        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < l.size()) {
                    // performing operation
                    progressBarStatus = i;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Updating the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                // performing operation if file is downloaded,
                if (progressBarStatus >= l.size()) {
                    // sleeping for 1 second after operation completed

                    //Intent i = new Intent(getApplicationContext(),CartActivity.class);
//                                startActivity(i);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    parseResults();

                    // close the progress bar dialog
                    progressBar.dismiss();
                }
            }
        }).start();
    }


    public void processBitmap(Bitmap bitmap,int ind) {
        String url = "https://api.ximilar.com/detection/v2/detect/";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        JSONObject jsonParams = new JSONObject();

        try {
            //Add string params

            jsonParams.put("task", "bfc83007-4aed-4f8a-922f-197ce94f9baa");
            jsonParams.put("version", 1);
            jsonParams.put("keep_prob",0.15 );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Create json array for filter
        JSONArray array = new JSONArray();

        //Create json objects for two filter Ids
        JSONObject jsonParam1 = new JSONObject();


        try {

            jsonParam1.put("_base64", imageString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Add the filter Id object to array
        array.put(jsonParam1);


        //Add array to main json object
        try {
            jsonParams.put("records", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                i++;
                result.put(String.valueOf(ind),response);
//                tv.setText(response.toString());



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Gallery.this, error.toString(), Toast.LENGTH_LONG).show();
//                tv.setText(error.toString());
                err++;
                if (err<12) {
                    processBitmap(bitmap,ind);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token 6094fab5d0e260d1d39b7e9f0a77b8b3648f4826");
                return headers;
            }
        };
        q.add(jsonObjectRequest);

    }
    public void parseResults(){
        int small=0,bot=0,top=0;
        String s = "";
        for(Map.Entry<String,JSONObject> res : result.entrySet()){
            s = String.valueOf(res);
            try {
                JSONArray ja = res.getValue().getJSONArray("records");
                JSONArray js = ja.getJSONObject(0).getJSONArray("_objects");
                Log.d("lenJSON",String.valueOf(ja));


                int len= js.length();
                for(int i=0;i<len;i++){
                    JSONObject entry = js.getJSONObject(i);
                    Log.d("entry",String.valueOf(entry));

                    String name = entry.getString("name");
                    if(name.equals("Lower")){
                        bot++;
                    }
                    if(name.equals("Small")){
                        small++;
                    }
                    if(name.equals("UpperWear")){
                        top++;
                    }
                    Log.d("objtsname",String.valueOf(name));

                }
                Log.d("small123",String.valueOf(small));
                s+=("\n");
                s+=("Samll:"+small+"   lower: "+bot+"  upper: "+top);
                sendInt(small,bot,top);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JsonExceptiona",String.valueOf(e));

            }
            JSONObject ja= null;
            try {
                JSONArray abc = new JSONArray();
                res.getValue().toJSONArray(abc);
                Log.d("res",String.valueOf(abc));


            } catch (JSONException e) {
                Log.d("resEx",String.valueOf(e));

                e.printStackTrace();
            }


        }

    }
    public void sendInt(int small,int bot ,int top){

        Intent i = new Intent(this,cart.class);
        i.putExtra("small",small);
        i.putExtra("bot",bot);
        i.putExtra("top",top);
        startActivity(i);

    }
}
