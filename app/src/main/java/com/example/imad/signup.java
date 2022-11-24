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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button signUp = findViewById(R.id.signUpStudent2);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email, password, name, roll;
                email = findViewById(R.id.email_up);
                password = findViewById(R.id.password_up);
                name = findViewById(R.id.name_up);
                roll = findViewById(R.id.roll_up);
                String em, pw, nm, ph;
                em = email.getText().toString();
                pw = password.getText().toString();
                nm = name.getText().toString();
                ph = roll.getText().toString();
                if (em.isEmpty() || pw.isEmpty() || nm.isEmpty()) {
                    Toast.makeText(signup.this, "failed to make user", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    mAuth.createUserWithEmailAndPassword(em, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               User us = new User(nm,ph);
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(us);
                                Intent intent = new Intent(getApplicationContext(), landing.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

}