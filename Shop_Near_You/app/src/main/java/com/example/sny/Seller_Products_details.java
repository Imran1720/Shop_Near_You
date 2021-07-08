package com.example.sny;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Seller_Products_details extends AppCompatActivity {

    ImageView iv;
    TextView name,cost,sdes,ldes;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products_details);

        iv= findViewById(R.id.prpic);
        name= findViewById(R.id.prname);
        cost= findViewById(R.id.prcost);
        sdes= findViewById(R.id.psd);
        ldes= findViewById(R.id.pdesc);

        id = getIntent().getStringExtra("proid");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().
                child("SNY").
                child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("PRODUCTS").child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(Seller_Products_details.this).
                        load(snapshot.child("prul").getValue(String.class)).
                        placeholder(R.drawable.ic_launcher_background).into(iv);

                name.setText(snapshot.child("prname").getValue(String.class));
                cost.setText(snapshot.child("prmincost").getValue(String.class));
                sdes.setText(snapshot.child("prsdes").getValue(String.class));
                ldes.setText(snapshot.child("prldes").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}