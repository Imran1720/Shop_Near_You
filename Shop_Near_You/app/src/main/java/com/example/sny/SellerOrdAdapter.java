package com.example.sny;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class SellerOrdAdapter extends RecyclerView.Adapter<SellerOrdAdapter.ViewHolder> {

    Context ct;
    ArrayList<OrdModel> list;

    public SellerOrdAdapter(Context ct, ArrayList<OrdModel> list) {
        this.ct = ct;
        this.list = list;
    }

    @NonNull
    @Override
    public SellerOrdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ct).inflate(R.layout.seller_orderverification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SellerOrdAdapter.ViewHolder holder, int position) {

        Glide.with(ct).load(list.get(position).getPurl()).into(holder.pic);
        holder.pname.setText(list.get(position).getPname());
        holder.pdes.setText(list.get(position).getPsdes());
        holder.pcost.setText(list.get(position).getPcost());
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
                holder.pstatus.setText("Canceled");
                holder.pstatus.setTextColor(Color.parseColor("#FFFF0000"));
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ORDERS").child(list.get(position).getSid()).
                        child(list.get(position).getPid());

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(list.get(position).getSid()).child("ORDERS").
                        child(list.get(position).getPid());

                db1.child("status").setValue("Canceled");
                db.child("status").setValue("Canceled");
            }
        });


        holder.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.pstatus.setText("Accepted");
                holder.pstatus.setTextColor(Color.parseColor("#13E300"));
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ORDERS").child(list.get(position).getSid()).
                        child(list.get(position).getPid());

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(list.get(position).getSid()).child("ORDERS").
                        child(list.get(position).getPid());

                db1.child("status").setValue("Accepted");
                db.child("status").setValue("Accepted");

            }
        });

        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ORDERS")
                .child(list.get(position).getSid()).child(list.get(position).getPid());
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.pstatus.setText(snapshot.child("status").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic,ok,view,cancel;
        TextView pname,pdes,pcost,pstatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.sprdpic);
            ok = itemView.findViewById(R.id.sok);
            view = itemView.findViewById(R.id.sview);
            cancel = itemView.findViewById(R.id.scancel);
            pname = itemView.findViewById(R.id.spname);
            pdes = itemView.findViewById(R.id.spsdes);
            pcost = itemView.findViewById(R.id.spcost);
            pstatus = itemView.findViewById(R.id.sstatus);

        }
    }
}
