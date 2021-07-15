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

public class CustomerOrder extends Fragment {

    RecyclerView rv;
    ArrayList<OrdModel> list;
    String id;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_order, container, false);

        rv =v.findViewById(R.id.corv);
        list = new ArrayList<>();
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(id).child("ORDERS");


        //set recycler view
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String prid,sid,stat;
                    prid = dataSnapshot.child("prid").getValue(String.class);
                    sid = dataSnapshot.child("sid").getValue(String.class);
                    stat = dataSnapshot.child("status").getValue(String.class);
                    setRV(prid,sid,stat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }

    private void setRV(String prid, String sid,String stat) {



        DatabaseReference dr=FirebaseDatabase.getInstance().getReference().child("SNY").child("PRODUCTS").child(prid);
        dr.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrdModel myModel = new OrdModel(snapshot.child("prul").getValue(String.class),
                        snapshot.child("prname").getValue(String.class),
                        snapshot.child("prsdes").getValue(String.class),
                        snapshot.child("prmincost").getValue(String.class),
                        snapshot.child("prid").getValue(String.class),sid,stat);
                list.add(myModel);
                CusOrdAdapter adapter = new CusOrdAdapter(getActivity(),list);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}