package com.example.imad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class existingOrders extends AppCompatActivity {
    ArrayList<Orders> orders;
    DatabaseReference databaseReference;
    OrderAdapter adapter;
    FirebaseAuth mAuth;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_orders);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        orders=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        UID=String.valueOf(mAuth.getCurrentUser().getUid());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_view);
        adapter = new OrderAdapter(orders,existingOrders.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            orders.clear();
            for(DataSnapshot sn:snapshot.getChildren()){
                Orders order = sn.getValue(Orders.class);
                if (UID.equals(order.getUid())&&order!=null) {
                    Log.d("abc123", order.getUid());
                    orders.add(order);
                }
            }


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
            }
        });

    }
    public void updatePay(){

    }
}