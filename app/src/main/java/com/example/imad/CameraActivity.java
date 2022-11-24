package com.example.imad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CameraActivity extends AppCompatActivity {
    List<Bitmap> l = new ArrayList<Bitmap>();
    Map<Integer,Integer> track;
    Map<String,JSONObject> result;
    RequestQueue q;
    int i;
    int imgViews;
    int j,err;

    AlertDialog.Builder builder;
    JSONObject fn;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    AlertDialog diag,diag2;
    ImageView display,del;
    int curr;
    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);

        }
        imgViews=0;
        Button rm = (Button)findViewById(R.id.remove);
        rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(l.size()==0){
                    Intent i = new Intent(getApplicationContext(),landing.class);
                    startActivity(i);
                }
                else{
                    l.remove(curr);

                    for (Map.Entry<Integer, Integer> entry : track.entrySet()) {
                        if(entry.getValue()>curr){
                        track.put(entry.getKey(),entry.getValue()-1);
                        }
                    }
                    i--;
                    LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
                    layout.removeView(del);
                    if(l.size()!=0){
                        display.setImageBitmap(l.get(0));
                    }
                    else{
                        display.setImageResource(R.drawable.cloth);
                    }
                }

            }
        });
        err=0;
        result = new HashMap<String,JSONObject>();
        track = new HashMap<Integer,Integer>();
        builder = new AlertDialog.Builder(CameraActivity.this);

        display = (ImageView)findViewById(R.id.dsp);

        //Button Two : No


        fn = new JSONObject();
        q = Volley.newRequestQueue(CameraActivity.this);
        i = 0;
        j=0;
        Button btn = (Button) findViewById(R.id.add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
        btn.performClick();
        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue q = Volley.newRequestQueue(CameraActivity.this);
                TextView tv = (TextView) findViewById(R.id.textView);

                for (int j=0;j<l.size();j++) {
                    processBitmap(l.get(j),j);
                }
                progressBar = new ProgressDialog(CameraActivity.this);
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
        });

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
                Toast.makeText(CameraActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
    public void addImgToFolder(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && false == Environment.isExternalStorageManager()) {
            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
        }
        File direct = new File(Environment.getExternalStorageDirectory() + "/LaundryImages");
        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/LaundryImages");
            wallpaperDirectory.mkdir();
        }

        String name = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        TextView tv = (TextView)findViewById(R.id.textView);

        File direct2 = new File(Environment.getExternalStorageDirectory() + "/LaundryImages/"+name);
        direct2.mkdir();
        int i=0;
        for (Bitmap bm : l) {
            i++;
//            tv.setText(String.valueOf(i));
            File file = new File(direct2,String.valueOf(i)+".jpg");

            try {
                FileOutputStream out = new FileOutputStream(file);
                if(file == null)tv.setText("null");
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
    public void parseResults() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == 100) {
            // BitMap is data structure of image file which store the image in memory
            if(data==null)return;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            display.setImageBitmap(photo);
            l.add(photo);

//            ImageView iv = (ImageView) findViewById(R.id.imageView);
//            iv.setImageBitmap(photo);
            LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
            ImageView imageView = new ImageView(this);
            imageView.setId(imgViews);
            track.put(imgViews,l.size()-1);
            imgViews++;
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageBitmap(photo);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    curr=0;
                    curr = (int) imageView.getId();
                    curr = track.get(curr);
                    display.setImageBitmap(l.get(curr));
                    del = imageView;
                }
            });
            imageView.setLayoutParams(params);
            layout.addView(imageView);

        }
    }



}




