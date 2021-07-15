 package com.example.sny;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Products_Customer extends AppCompatActivity {

    ImageView iv;
    TextView name,cost,sdes,ldes;
    String id,sid;
    ProgressDialog pd;
    DatabaseReference ccart,databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_customer);

        id= getIntent().getStringExtra("prodid");


        iv= findViewById(R.id.prpic);
        name= findViewById(R.id.prname);
        cost= findViewById(R.id.prcost);
        sdes= findViewById(R.id.psd);
        ldes= findViewById(R.id.pdesc);

        ccart = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("CART").child(id);
        databaseReference = FirebaseDatabase.getInstance().getReference().
                child("SNY").
                child("PRODUCTS").child(id);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(Products_Customer.this).
                        load(snapshot.child("prul").getValue(String.class)).
                        placeholder(R.drawable.ic_launcher_background).into(iv);

                name.setText(snapshot.child("prname").getValue(String.class));
                cost.setText(snapshot.child("prmincost").getValue(String.class));
                sdes.setText(snapshot.child("prsdes").getValue(String.class));
                ldes.setText(snapshot.child("prldes").getValue(String.class));

                sid=snapshot.child("sid").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void buy(View view) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ORDERS");
        //in customer->orders=>product id,seller id
        reference.child(id).child("prid").setValue(id);
        reference.child(id).child("sid").setValue(sid);
        reference.child(id).child("status").setValue("Processing...");

        //in seller->orders=>product id,customr id
        DatabaseReference Sreference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                child(sid).child("ORDERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Sreference.child(id).child("prid").setValue(id);
        Sreference.child(id).child("status").setValue("Processing");
        Sreference.child(id).child("cid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //remove item from cart on order
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("CART");
        databaseReference.child(id).removeValue();
        Toast.makeText(this, "PRODUCT HAS BEEN ORDERED", Toast.LENGTH_SHORT).show();

    }

    public void addcart(View view) {

        ccart.child("prid").setValue(id);
        Toast.makeText(this, "PRODUCT WAS ADDED TO THE CART", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,HomePage.class));
    }
}