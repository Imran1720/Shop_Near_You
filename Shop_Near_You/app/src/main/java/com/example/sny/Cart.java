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

public class Cart extends Fragment {

    RecyclerView rv;
    ArrayList<MyModel> list;
    ArrayList<String> prids;
    DatabaseReference cart,prd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        rv = view.findViewById(R.id.cartrv);
        list = new ArrayList<>();


        prd = FirebaseDatabase.getInstance().getReference().child("SNY").child("PRODUCTS");

        cart = FirebaseDatabase.getInstance().getReference().child("SNY").
                child("USERS").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("CART");

        cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    addRV(dataSnapshot.child("prid").getValue(String.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


        return view;
    }


    public void addRV(String prid) {


        list.clear();
        prd.child(prid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyModel myModel = new MyModel(snapshot.child("prul").getValue(String.class),
                        snapshot.child("prname").getValue(String.class),
                        snapshot.child("prmincost").getValue(String.class),
                        snapshot.child("prsdes").getValue(String.class),
                        snapshot.child("prid").getValue(String.class));
                list.add(myModel);
                CartAdapter adapter = new CartAdapter(getActivity(),list);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}