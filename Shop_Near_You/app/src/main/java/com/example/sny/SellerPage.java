package com.example.sny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SellerPage extends AppCompatActivity {

    DatabaseReference cref;

    ImageView profile;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_page);



        changeto(new Seller_Products_List());
        profile = findViewById(R.id.propic);
        auth = FirebaseAuth.getInstance();
        cref =FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(auth.getCurrentUser().getUid());


        SellerView();

    }

    private void SellerView() {
        cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(SellerPage.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sigout(View view) {
        auth.signOut();
        startActivity(new Intent(SellerPage.this,MainActivity.class));
    }


    public void sproducts(View view) {
        changeto(new Seller_Products_List());
    }

    public void saddprod(View view) {
        changeto(new Seller_Products_Add());
    }



    public void sprodstat(View view) {
        changeto(new Seller_Products_Stat());
    }



    private void changeto(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLay,fragment);
        fragmentTransaction.commit();
    }
}