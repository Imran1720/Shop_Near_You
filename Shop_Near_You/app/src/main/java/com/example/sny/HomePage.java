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

public class HomePage extends AppCompatActivity {

    DatabaseReference cref;
    ImageView profile;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        auth = FirebaseAuth.getInstance();
        profile = findViewById(R.id.propic);
        cref = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(auth.getCurrentUser().getUid());
        customerView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.hffl,new Home_Products()).commit();




    }

    private void LoadFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.hffl,fragment).addToBackStack(null).commit();
    }

    private void customerView() {
        cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(HomePage.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sigout(View view) {
        auth.signOut();
        startActivity(new Intent(HomePage.this,MainActivity.class));
    }

    public void cart(View view) {

        LoadFragment(new Cart());

    }

    public void home(View view) {
        LoadFragment(new Home_Products());
    }
}