package com.example.sny;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Products_Customer extends AppCompatActivity {

    ImageView iv;
    TextView name,cost,sdes,ldes;
    String id,uid;
    ProgressDialog pd;
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().
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

                uid=snapshot.child("sid").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void buy(View view) {


    }

    public void addcart(View view) {
    }
}