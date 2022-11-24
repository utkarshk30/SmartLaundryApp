package com.example.imad;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{


    private ArrayList<Orders> orders;
    RecyclerView recyclerView;
    String del;
    AlertDialog.Builder builder2,builder;
    AlertDialog diag,diag2;
    ViewGroup par;
    DatabaseReference mDatabase;
    Context existCon;

    public OrderAdapter(ArrayList<Orders> orders, Context context) {

        this.orders = orders;
        this.existCon=context;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.myorders, parent, false);
        // holder.setIsRecyclable(false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        par = parent;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Orders order= orders.get(position);
        holder.date.setText("Date: " +order.getDate());
        //holder.date.setText("Order ID: " +order.get());
        holder.upper.setText(String.valueOf(order.getUpper())+"x Upper");
        holder.lower.setText(String.valueOf(order.getLower())+"x Lower");
        holder.small.setText(String.valueOf(order.getSmall())+"x Small");
        int a=order.getUpper()*10;
        int b=order.getLower()*10;
        int c=order.getSmall()*5;
        holder.upperprice.setText("Rs "+String.valueOf(a));
        holder.lowerprice.setText("Rs "+String.valueOf(b));
        holder.smallprice.setText("Rs "+String.valueOf(c));
        holder.orderid.setText("Order No. : "+String.valueOf(order.getOrderid()));
        builder2= new AlertDialog.Builder(existCon);
        builder = new AlertDialog.Builder(existCon);


        builder.setTitle("Remove Image");


        builder.setMessage("Remove Order From DataBase?");


        //Button One : Yes
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabase.child("Orders").child(del).removeValue();

            }
        });


        //Button Two : No
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        diag = builder.create();

        if(order.isPaid()==false)
        {

            holder.pay.setText("Total Amount : Rs."+ order.getPrice());
            holder.pay.setEnabled(true);
            if(order.isReady()==false)
            {
                holder.deli.setText("Order Not Ready");
                holder.deli.setEnabled(false);
            }
            else{

                holder.deli.setText("Complete Payment To Collect Order");
                holder.deli.setEnabled(false);

            }

        }
        else
        {
            holder.pay.setText("PAID");
            holder.pay.setEnabled(false);
            if(order.isReady()==false)
            {
                holder.deli.setText("Order Not Ready");
                holder.deli.setEnabled(false);
            }
            else{

                holder.deli.setText("Collect");
                holder.deli.setEnabled(true);

            }
        }
        if(order.isDelivered()==true){
            holder.deli.setEnabled(false);
            holder.deli.setText("Delivered!!");
        }
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setPaid(true);
                mDatabase.child("Orders").child(String.valueOf(order.getOrderid())).setValue(order);
//                if (existCon instanceof existingOrders) {
//                    ((existingOrders)existCon).updatePay();
//                }

            }
        });
        holder.deli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("call","call");
                order.setDelivered(true);
                mDatabase.child("Orders").child(String.valueOf(order.getOrderid())).setValue(order);


            }
        });
        holder.cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order.isReady()==true&&order.isPaid()==true&&order.isDelivered()==true){

                    del = String.valueOf(order.getOrderid());
                    diag.show();

                }
                else{
                    if(order.isPaid()==false){
                        builder2.setTitle("PAYMENT");
                        builder2.setMessage("Order cannot be deleted since Payment is Not Completed Yet");
                        diag2 = builder2.create();
                        diag2.show();
                    }
                    else if(order.isDelivered()==false){
                        builder2.setTitle("COLLECT");
                        builder2.setMessage("Please Collect the order to delete this Receipt");
                        diag2 = builder2.create();
                        diag2.show();

                    }
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderid,date,phone,upper,lower,small,price,status,upperprice,smallprice,lowerprice;
        public Button pay,deli;
        public CardView cr;
        public ViewHolder(View itemView) {
            super(itemView);
            this.orderid=(TextView) itemView.findViewById(R.id.orderID) ;
            this.date = (TextView) itemView.findViewById(R.id.date);

            this.upperprice = (TextView) itemView.findViewById(R.id.upperprice);
            this.lowerprice = (TextView) itemView.findViewById(R.id.lowerprice);
            this.smallprice = (TextView) itemView.findViewById(R.id.smallprice);

            this.upper = (TextView) itemView.findViewById(R.id.upper);
            this.lower = (TextView) itemView.findViewById(R.id.lower);
            this.small = (TextView) itemView.findViewById(R.id.small);

            this.pay=(Button) itemView.findViewById(R.id.payment);
            this.deli=(Button) itemView.findViewById(R.id.delivery);
            cr = (CardView) itemView.findViewById(R.id.relativeLayout);
        }
    }
}