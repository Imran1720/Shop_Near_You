package com.example.sny;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CusOrdAdapter extends RecyclerView.Adapter<CusOrdAdapter.ViewHolder> {

    Context ct;
    ArrayList<OrdModel> list;

    public CusOrdAdapter(Context ct, ArrayList<OrdModel> list) {
        this.ct = ct;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ct).inflate(R.layout.cusorder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CusOrdAdapter.ViewHolder holder, int position) {

        Glide.with(ct).load(list.get(position).getPurl()).into(holder.pic);
        holder.name.setText(list.get(position).getPname());
        holder.sdes.setText(list.get(position).getPsdes());
        holder.cost.setText("₹ "+list.get(position).getPcost());
        holder.stat.setText(list.get(position).getStat());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder a = new AlertDialog.Builder(ct);
                View co = LayoutInflater.from(ct).inflate(R.layout.cosdisplay,null,false);
                TextView vhead,vname,vnumber,vemail,vplace,vnote;
                vhead =co.findViewById(R.id.vhead);
                vname =co.findViewById(R.id.vsname);
                vnumber =co.findViewById(R.id.vsnumber);
                vemail =co.findViewById(R.id.vsemail);
                vplace =co.findViewById(R.id.vsplace);
                vnote = co.findViewById(R.id.note);

                DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(list.get(position).getSid());
                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        vhead.setText(snapshot.child("actype").getValue(String.class)+" DETAILS");
                        vname.setText("Name : "+snapshot.child("name").getValue(String.class));
                        vnumber.setText("Phone : +91"+snapshot.child("phone").getValue(String.class));
                        vemail.setText("Email : "+snapshot.child("mail").getValue(String.class));
                        vplace.setText("place : "+snapshot.child("village").getValue(String.class));
                        vnote.setText("Note : please try to contact "+snapshot.child("actype").getValue(String.class).toLowerCase()+" using above details for confirmation.");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                a.setView(co);
                a.show();
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                d.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ORDERS").child(list.get(position).getPid());

                        DatabaseReference dr2= FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                                child(list.get(position).getSid()).child("ORDERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(list.get(position).pid);

                        dr.onDisconnect().removeValue();
                        dr2.onDisconnect().removeValue();


                        Toast.makeText(ct, "ORDER HAS BEEN CANCLED", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });





    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pic,view,cancel;
        TextView name,sdes,cost,stat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.prdpic);
            view = itemView.findViewById(R.id.view);
            cancel = itemView.findViewById(R.id.cancel);
            name = itemView.findViewById(R.id.pname);
            sdes = itemView.findViewById(R.id.psdes);
            cost = itemView.findViewById(R.id.pcost);
            stat = itemView.findViewById(R.id.status);

        }
    }
}
