package com.example.imad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    boolean ad;
    FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ad=false;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        Button signUpStudent = findViewById(R.id.signUpStudent);
        if(mAuth.getUid()!=null){
        db.getReference().child("Users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ad = snapshot.child("admin").getValue(Boolean.class);
                if(ad==true){
                    Intent intent = new Intent(getApplicationContext(), AdminView.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), landing.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        }

        signUpStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminView.class);
                startActivity(intent);
            }
        });
        Button signIn = findViewById(R.id.signIn);

//        if (mAuth.getCurrentUser() != null) {
//            if(ad==true){
//                Intent intent = new Intent(getApplicationContext(), AdminView.class);
//                startActivity(intent);
//            }
//            else{
//                Intent intent = new Intent(this, landing.class);
//                startActivity(intent);
//            }


            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText email = findViewById(R.id.email_in);
                    EditText password = findViewById(R.id.password_in);
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getBaseContext(), login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

    }
