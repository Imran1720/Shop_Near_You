package com.example.sny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_users_detail extends AppCompatActivity {

    TextView na,m,p,a,ad;
    ImageView pic;
    String id;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_detail);


        na = findViewById(R.id.auname);
        m = findViewById(R.id.aumail);
        p = findViewById(R.id.auphone);
        a = findViewById(R.id.auactype);
        ad = findViewById(R.id.auaddress);
        pic = findViewById(R.id.aupic);
        id = getIntent().getStringExtra("id");

        ref = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(id);

        display();


    }

    private void display() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                na.setText(snapshot.child("name").getValue(String.class));
                m.setText(snapshot.child("mail").getValue(String.class));
                p.setText(snapshot.child("phone").getValue(String.class));
                a.setText(snapshot.child("actype").getValue(String.class));
                ad.setText(snapshot.child("address").getValue(String.class));
                Glide.with(admin_users_detail.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person).into(pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}