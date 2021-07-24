package com.example.sny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminView extends AppCompatActivity {


    DrawerLayout drawerLayout;
    Toolbar toolbar;

    TextView sbname,sbmail;

    DatabaseReference aref;
    ImageView p,pic;
    BottomNavigationView bv;
    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);


        drawerLayout =findViewById(R.id.drawer_layout);


        p =findViewById(R.id.profilepic);
        pic =findViewById(R.id.pic);

        sbmail=findViewById(R.id.sbemail);
        sbname=findViewById(R.id.sbname);

        bv = findViewById(R.id.bnv);
       fl = findViewById(R.id.adfl);

        String uid;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        aref = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(uid);

        adminview();
        //default fragment in admin
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adfl,new Admin_Customer()).commit();
        loadframe();

    }


    //load fragment in admin view
    private void loadframe() {

        bv.setOnItemSelectedListener(item -> {
            Fragment f = null;
            switch (item.getItemId()){

                case R.id.acus:f=new Admin_Customer();
                break;
                case R.id.asel:f=new Admin_Seller();
                break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.adfl,f).commit();

            return true;
        }
        );

    }

    public void openDrawer(View v){

        drawerLayout.openDrawer(GravityCompat.START);
    }


    public void closeDrawer(View view)
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    //load admin details
    private void adminview() {
        aref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(AdminView.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(p);
                Glide.with(AdminView.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(pic);
                sbmail.setText(snapshot.child("mail").getValue(String.class));
                sbname.setText(snapshot.child("name").getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //logout
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AdminView.this,MainActivity.class));
        finish();
    }

    //homepage
    public void home(View view) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adfl,new Admin_Customer()).commit();
        closeDrawer(drawerLayout);

    }
    //fragment showing all customers
    public void customers(View view) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adfl,new Admin_Customer()).commit();
        closeDrawer(drawerLayout);
    }
    //fragment showing all sellers
    public void sellers(View view) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adfl,new Admin_Seller()).commit();
        closeDrawer(drawerLayout);
    }


}