package com.example.sny;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    Context ct;
    ArrayList<MyModel> list;
    String id;

    public CustomerAdapter(Context ct, ArrayList<MyModel> list) {
        this.ct = ct;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ct).inflate(R.layout.product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {

        Glide.with(ct).load(list.get(position).getPrul()).placeholder(R.drawable.ic_launcher_background).into(holder.iv);
        holder.name.setText(list.get(position).getPrname());
        holder.des.setText(list.get(position).getPrsdes());
        holder.cost.setText("₹"+list.get(position).getPrmincost());


        holder.l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ct,Seller_Products_details.class);
                i.putExtra("proid",list.get(position).getPrid());
                ct.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView name,cost,des;
        LinearLayout l;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ppic);
            name = itemView.findViewById(R.id.pname);
            cost = itemView.findViewById(R.id.pcost);
            des = itemView.findViewById(R.id.old);
            l= itemView.findViewById(R.id.productlayout);
        }
    }
}
