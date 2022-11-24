package com.example.imad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {


    private ArrayList<Orders> orders;
    RecyclerView recyclerView;
    String del;
    AlertDialog.Builder builder2, builder;
    AlertDialog diag, diag2;
    ViewGroup par;
    DatabaseReference mDatabase;
    Context existCon;
    String nm="",ph="";

    public PendingAdapter(ArrayList<Orders> orders) {

        this.orders = orders;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        // holder.setIsRecyclable(false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        par = parent;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PendingAdapter.ViewHolder holder, int position) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        Orders order= orders.get(position);
        String UID=order.getUid();

        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sn: snapshot.getChildren())
                {
                    if(UID.equals(String.valueOf(sn.getKey())))
                    {
                        User u=sn.getValue(User.class);
                        nm=u.getName();
                        ph=u.getMobile();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.name.setText("Order No. : "+ String.valueOf(order.getOrderid()));
        holder.date.setText("Date: " +order.getDate());
        holder.phone.setText("Phone Num. : " +ph);
        holder.upper.setText("Upper: " +String.valueOf(order.getUpper()));
        holder.lower.setText("Lower: " +String.valueOf(order.getLower()));
        holder.price.setText("Price: " +String.valueOf(order.getPrice()));
        holder.small.setText("Small: " +String.valueOf(order.getSmall()));
        if(order.isPaid()==true)
        {
            holder.paid.setText("Payment Completed");
        }
        else
        {
            holder.paid.setText("Payment Pending....");

        }


      holder.red.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              order.setReady(true);
              mDatabase.child("Orders").child(String.valueOf(order.getOrderid())).setValue(order);
          }
      });


    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,date,phone,upper,lower,small,price,paid;
        public CardView cr;
        public Button red;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.phone = (TextView) itemView.findViewById(R.id.phone);
            this.upper = (TextView) itemView.findViewById(R.id.upper);
            this.lower = (TextView) itemView.findViewById(R.id.lower);
            this.small = (TextView) itemView.findViewById(R.id.small);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.paid=   (TextView) itemView.findViewById(R.id.paid);
            cr = (CardView) itemView.findViewById(R.id.relativeLayout);
            red=(Button) itemView.findViewById(R.id.ready);
        }
    }
}



