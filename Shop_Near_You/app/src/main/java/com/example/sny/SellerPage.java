package com.example.sny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
    ImageView iv,pic;
    TextView name,mail;
    DrawerLayout drawerLayout;


    //Database objects
    String uid;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_page);


        bv = findViewById(R.id.bottom_nav);
        fl = findViewById(R.id.framl);
        iv= findViewById(R.id.profilepic);
        pic= findViewById(R.id.pic);

        drawerLayout = findViewById(R.id.s_drawer_layout);
        name = findViewById(R.id.sbname);
        mail = findViewById(R.id.sbemail);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(uid);
        Sellerview();

        //default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.framl,new Seller_Products_List()).commit();

        LoadFragment();

    }

    //Bottom navigation view selection
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

                case R.id.orders:
                    fragment = new Seller_Products_orders();
                    break;
            }


            getSupportFragmentManager().beginTransaction().replace(R.id.framl, fragment).commit();

            return true;
        });

    }

    //load profile pic,name and mail
    private void Sellerview() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(SellerPage.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(iv);
                Glide.with(SellerPage.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(pic);

                name.setText(snapshot.child("name").getValue(String.class));
                mail.setText(snapshot.child("mail").getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //load product list
    public  void home(View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.framl, new Seller_Products_List()).commit();
        closeDrawer(drawerLayout);
    }

    //load orders list
    public  void order(View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.framl,new Seller_Products_orders()).commit();
        closeDrawer(drawerLayout);
    }

    //load products list
    public  void products(View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.framl,new Seller_Products_List()).commit();
        closeDrawer(drawerLayout);
    }

    //log out
    public  void logout(View view)
    {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SellerPage.this,MainActivity.class));
        finish();

    }

    //Drawer layout open
    public void openDrawer(View view)
    {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    //Drawer layout close
    public void closeDrawer(View view)
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}