package com.example.sny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
    ImageView profile,pic;
    TextView name,mail;
    FirebaseAuth auth;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        drawerLayout = findViewById(R.id.h_drawer_layout);
        auth = FirebaseAuth.getInstance();
        profile = findViewById(R.id.profilepic);
        pic = findViewById(R.id.pic);
        name = findViewById(R.id.sbname);
        mail = findViewById(R.id.sbemail);
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
                Glide.with(HomePage.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(pic);
                name.setText(snapshot.child("name").getValue(String.class));
                mail.setText(snapshot.child("mail").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logout(View view) {
        auth.signOut();
        startActivity(new Intent(HomePage.this,MainActivity.class));
        finish();
    }

    public void cart(View view) {
        LoadFragment(new Cart());
        closeDrawer(drawerLayout);
    }

    public void order(View view)
    {
        Toast.makeText(this, "this is your orders", Toast.LENGTH_SHORT).show();
        closeDrawer(drawerLayout);
    }

    public void home(View view) {

        LoadFragment(new Home_Products());
        closeDrawer(drawerLayout);
    }

    public  void openDrawer(View view)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public  void closeDrawer(View view){
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }




}