package com.example.sny;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home_Products extends Fragment {

    RecyclerView rv;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ArrayList list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home__products, container, false);

        rv = v.findViewById(R.id.rv);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("PRODUCTS");

        list =new ArrayList<>();

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

                CustomerAdapter adapter = new CustomerAdapter(getActivity(),list);
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