package com.example.sny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context ct;
    ArrayList<MyModel> list;
    String sid;



    public CartAdapter(Context ct, ArrayList<MyModel> list) {
        this.ct = ct;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ct).inflate(R.layout.cartproducts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        Glide.with(ct).load(list.get(position).getPrul()).placeholder(R.drawable.ic_launcher_background).into(holder.iv);
        holder.name.setText(list.get(position).getPrname());
        holder.des.setText(list.get(position).getPrsdes());
        holder.cost.setText("₹"+list.get(position).getPrmincost());

        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =list.get(position).getPrid();
                getProductSeller(id);


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("CART");
                databaseReference.child(list.get(position).getPrid()).removeValue();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framl, new Cart()).commit();
                Toast.makeText(ct, "PRODUCT HAS BEEN ORDERED", Toast.LENGTH_SHORT).show();

            }
        });

        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("CART");
                databaseReference.child(list.get(position).getPrid()).removeValue();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framl, new Cart()).commit();
                Toast.makeText(ct, "PRODUCTS REMOVED FROM CART", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getProductSeller(String id) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("PRODUCTS").
                child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               sid = snapshot.child("sid").getValue(String.class);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ORDERS");

                reference.child(id).child("prid").setValue(id);
                reference.child(id).child("status").setValue("Processing...");
                reference.child(id).child("sid").setValue(sid);


                DatabaseReference Sreference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").
                        child(sid).child("ORDERS");

                Sreference.child(id).child("prid").setValue(id);
                Sreference.child(id).child("status").setValue("Processing");
                Sreference.child(id).child("cid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        ImageView iv;
        TextView name,cost,des;
        Button b1,b2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ppic);
            name = itemView.findViewById(R.id.pname);
            cost = itemView.findViewById(R.id.pcost);
            des = itemView.findViewById(R.id.old);
            b1= itemView.findViewById(R.id.cpbuy);
            b2=itemView.findViewById(R.id.cpremove);
        }

    }
}
