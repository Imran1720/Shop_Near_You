package com.example.sny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminView extends AppCompatActivity {

    RecyclerView rv;
    DatabaseReference reference,aref;
    ArrayList<MyModel> list;


    ImageView p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        rv = findViewById(R.id.rv);
        list = new ArrayList<>();
        p = findViewById(R.id.profilepic);
        String propic,uid;



        reference = FirebaseDatabase.getInstance().getReference("SNY").child("USERS");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        aref = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(uid);
        adminview();



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    MyModel myModel = new MyModel(dataSnapshot.child("url").getValue(String.class),
                            dataSnapshot.child("name").getValue(String.class),
                            dataSnapshot.child("mail").getValue(String.class),
                            dataSnapshot.child("phone").getValue(String.class),
                            dataSnapshot.child("actype").getValue(String.class),
                            dataSnapshot.child("id").getValue(String.class));

                    list.add(myModel);


                                    }

                MyAdapter adapter = new MyAdapter(AdminView.this,list);



                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(AdminView.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdminView.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
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
}