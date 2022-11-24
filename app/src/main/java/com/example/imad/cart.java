//package com.example.imad;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ServerValue;
//import com.google.firebase.database.ValueEventListener;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.Map;
//
//public class cart extends AppCompatActivity {
//    TextView top,totaltop;
//    TextView bottom,totalbot;
//    TextView small,totalsmall;
//    TextView total;
//    int up,sm,lo,tot;
//    String topval,botval,smallval,date;
//    int prbot = 10,prtop=10,prsmall=5;
//    Button addorder,upd,upi,lowi,lowd,smalli,smalld;
//    private DatabaseReference orders;
//    LocalDate currdate;
//    int id = 2;
//
//
//
//
//        @Override
//        protected void onCreate (Bundle savedInstanceState){
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_cart);
//
//            top = (TextView) findViewById(R.id.Top);
//            bottom = (TextView)findViewById(R.id.Bottom);
//            small = (TextView) findViewById(R.id.small);
//            total = (TextView) findViewById(R.id.total);
//
//            totaltop=(TextView)findViewById(R.id.totTop);
//            totalbot=(TextView)findViewById(R.id.totBot);
//            totalsmall=(TextView)findViewById(R.id.totSmall);
//
//            addorder=(Button) findViewById(R.id.addOrder);
//            upd=(Button) findViewById(R.id.decTop);
//            upi=(Button) findViewById(R.id.incTop);
//            lowd=(Button) findViewById(R.id.decBot);
//            lowi=(Button) findViewById(R.id.incBot);
//            smalld=(Button) findViewById(R.id.decSmall);
//            smalli=(Button) findViewById(R.id.incSmall);
//            Bundle extras = getIntent().getExtras();
//            smallval=extras.getString("small");
//            topval=extras.getString("upper");
//            botval=extras.getString("lower");
//            up = Integer.parseInt(topval);
//            lo = Integer.parseInt(botval);
//            sm = Integer.parseInt(smallval);
//            int x =lo*prbot,y=up*prtop,z=sm*prsmall;
//            tot = x+y+z;
//            update();
//
//            addorder = findViewById(R.id.addOrder);
//            orders = FirebaseDatabase.getInstance().getReference("Orders");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                currdate = LocalDate.now();
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                date = currdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//            }
//            upd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    up--;
//                    up=Math.max(0,up);
//                    update();
//                }
//            });
//            upi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    up++;
//                    update();
//                }
//            });
//            lowd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    lo--;
//                    lo=Math.max(0,lo);
//                    update();
//                }
//            });
//            lowi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    lo++;
//                    update();
//                }
//            });
//            smalld.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    sm--;
//                    sm=Math.max(0,sm);
//                    update();
//                }
//            });
//            smalli.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    sm++;
//                    update();
//                }
//            });
//            addorder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                    String UID = mAuth.getUid();
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
////                    Orders od = new Orders(date,);
//                    Map<String, Object> mp = new HashMap<>();
//                    orders.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Integer cnt = snapshot.child("total").getValue(Integer.class);
//                            database.getReference().child("Applications").child(UID).child(cnt.toString()).setValue(app);
//                            mp.put("num_app", ServerValue.increment(1));
//                            database.getReference().child("Users").child(UID).updateChildren(mp);
//                            Toast.makeText(cart.this, "Order Created", Toast.LENGTH_SHORT).show();
//                            try {
//                                cart.this.wait(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            Intent intent = new Intent(getApplicationContext(), landing.class);
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            });
//
//           }
//
//           public void update(){
//
//            int x =lo*prbot,y=up*prtop,z=sm*prsmall;
//               totalbot.setText("Rs. "+String.valueOf(x));
//               totaltop.setText("Rs. "+String.valueOf(y));
//               totalsmall.setText("Rs. "+String.valueOf(z));
//               total.setText("Rs. "+String.valueOf(x+y+z));
//               top.setText(String.valueOf(up));
//               bottom.setText(String.valueOf(lo));
//               small.setText(String.valueOf(sm));
//           }
//
//
//}