package com.example.sny;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SellerPage extends AppCompatActivity {


    BottomNavigationView bv;
    FrameLayout fl;
    ImageView iv;

    //network objects
    FirebaseAuth auth;
    String uid;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_page);

        bv = findViewById(R.id.bottom_nav);
        fl = findViewById(R.id.framl);
        iv= findViewById(R.id.profpic);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(uid);
        Sellerview();

        getSupportFragmentManager().beginTransaction().replace(R.id.framl,new Seller_Products_List()).commit();

        LoadFragment();

    }

    private void LoadFragment() {
        bv.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.slist:
                    fragment = new Seller_Products_List();
                    break;

                case R.id.sadd:
                    fragment = new Seller_Products_Add();
                    break;

                case R.id.sstat:
                    fragment = new Seller_Products_Stat();
                    break;
            }


            getSupportFragmentManager().beginTransaction().replace(R.id.framl, fragment).commit();

            return true;
        });

    }

    private void Sellerview() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(SellerPage.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(iv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}