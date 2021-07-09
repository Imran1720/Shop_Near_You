package com.example.sny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {


    RecyclerView rv;
    DatabaseReference databaseReference,cref;
    MyAdapter adapter;
    ImageView profile;
    ArrayList<MyModel> list;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        rv = findViewById(R.id.crv);
        profile = findViewById(R.id.propic);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("PRODUCTS");
        cref =FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(auth.getCurrentUser().getUid());




        list =new ArrayList<>();
        adapter = new MyAdapter(this,list);


        customerView();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    MyModel myModel = new MyModel(dataSnapshot.child("prul").getValue(String.class),
                            dataSnapshot.child("prname").getValue(String.class),
                            dataSnapshot.child("prmincost").getValue(String.class),
                            dataSnapshot.child("prsdes").getValue(String.class),
                            dataSnapshot.child("prid").getValue(String.class));
                    list.add(myModel);
                }

                CustomerAdapter adapter = new CustomerAdapter(HomePage.this,list);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(HomePage.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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


    }
}