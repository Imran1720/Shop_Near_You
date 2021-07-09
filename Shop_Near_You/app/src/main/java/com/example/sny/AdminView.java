package com.example.sny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


    DatabaseReference aref;
    ImageView p;
    BottomNavigationView bv;
    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        bv = findViewById(R.id.bnv);
       fl = findViewById(R.id.adfl);
        p = findViewById(R.id.profilepic);
        String uid;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        aref = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(uid);
        adminview();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adfl,new Admin_Customer()).commit();
        loadframe();

    }

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

    private void adminview() {
        aref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(AdminView.this).load(snapshot.child("url").getValue(String.class)).placeholder(R.drawable.ic_person_white).into(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AdminView.this,MainActivity.class));
    }

    public void viewdetail(View view) {
        Intent i = new Intent(this,admin_users_detail.class);
        i.putExtra("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
    }
}