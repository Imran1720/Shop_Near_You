package com.example.sny;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Seller_Products_orders extends Fragment {
    View v;
    RecyclerView rv;
    ArrayList<OrdModel> list;
    String user;
    DatabaseReference dr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_seller__products__orders, container, false);
        rv = v.findViewById(R.id.corv);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        list = new ArrayList<>();
        dr = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(user).child("ORDERS");

        //set order recycler view
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String key = dataSnapshot.getKey();
                    if (!key.equals(null))
                    {
                        getorder(key);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return v;
    }

    private void getorder(String key) {

        dr.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot: snapshot.getChildren())
               {

                   String pid,cid,status;
                   pid = dataSnapshot.child("prid").getValue(String.class);
                   cid = dataSnapshot.child("cid").getValue(String.class);
                   status = dataSnapshot.child("status").getValue(String.class);


                   setPDetails(pid,cid,status);

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setPDetails(String pid, String cid, String status) {



        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("SNY").child("PRODUCTS").child(pid);
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrdModel model = new OrdModel(snapshot.child("prul").getValue(String.class),
                        snapshot.child("prname").getValue(String.class),
                        snapshot.child("prsdes").getValue(String.class),
                        snapshot.child("prmincost").getValue(String.class),
                        snapshot.child("prid").getValue(String.class),
                        cid,status);
                list.add(model);


                SellerOrdAdapter adapter = new SellerOrdAdapter(getActivity(),list);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}