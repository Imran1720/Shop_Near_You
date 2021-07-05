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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context ct;
    ArrayList<MyModel> list;

    public MyAdapter(Context ct, ArrayList<MyModel> list) {
        this.ct = ct;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ct).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {


        Glide.with(ct).load(list.get(position).getUrl()).placeholder(R.drawable.ic_person).into(holder.iv);
        holder.name.setText(list.get(position).getName());
        holder.mail.setText(list.get(position).getMail());
        holder.phone.setText(list.get(position).getPhone());
        holder.actype.setText(list.get(position).getActype());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ct,admin_users_detail.class);
                i.putExtra("id",list.get(position).getId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ct.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,mail,phone,actype;
        ImageView iv;
        LinearLayout ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dname);
            mail = itemView.findViewById(R.id.demail);
            phone = itemView.findViewById(R.id.dnumber);
            actype = itemView.findViewById(R.id.dactype);

            ll= itemView.findViewById(R.id.llayoyt);

            iv = itemView.findViewById(R.id.dpic);
        }
    }
}
