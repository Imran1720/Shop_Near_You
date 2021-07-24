package com.example.sny;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Customer extends Fragment {

    RecyclerView rv;
    DatabaseReference databaseReference;
    ArrayList<MyModel> list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_admin__customer, container, false);

        rv = v.findViewById(R.id.facrv);

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS");

        //apply rv in admin customer  fragment
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(!dataSnapshot.child("actype").getValue(String.class).equals("ADMIN") && !dataSnapshot.child("actype").getValue(String.class).equals("SELLER"))
                    {
                        MyModel myModel = new MyModel(dataSnapshot.child("url").getValue(String.class),
                                dataSnapshot.child("name").getValue(String.class),
                                dataSnapshot.child("mail").getValue(String.class),
                                dataSnapshot.child("phone").getValue(String.class),
                                dataSnapshot.child("actype").getValue(String.class),
                                dataSnapshot.child("id").getValue(String.class));

                        list.add(myModel);
                    }
                }

                MyAdapter adapter = new MyAdapter(getActivity(),list);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        return v;
    }
}