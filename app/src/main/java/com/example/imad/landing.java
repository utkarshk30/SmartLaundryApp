package com.example.imad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Button bt = (Button) findViewById(R.id.existing);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),existingOrders.class);
                startActivity(i);
            }
        });
    }

    public void createNew(View view) {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);

    }
}