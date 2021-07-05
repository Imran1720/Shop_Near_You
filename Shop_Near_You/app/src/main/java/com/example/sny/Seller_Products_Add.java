package com.example.sny;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.sny.databinding.FragmentSellerProductsAddBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;


public class Seller_Products_Add extends Fragment {
    View v;

    FragmentSellerProductsAddBinding binding;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String id, pid;
    Random random;

    Uri uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_seller__products__add, container, false);
        binding = DataBindingUtil.setContentView(getActivity(),R.layout.fragment_seller__products__add);
        storageReference = FirebaseStorage.getInstance().getReference().child("SNY_USER_IMAGES/"+ UUID.randomUUID().toString());
        random=new Random();

        id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        pid =""+id+random.nextInt(1000)+2;
        Toast.makeText(getActivity(), ""+pid, Toast.LENGTH_SHORT).show();

        ActivityResultLauncher<String> mgetcontent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                uri = result;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                    binding.propic.setImageBitmap(bitmap);
                    binding.propic.setBackground(null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgetcontent.launch("image/*");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(id).child("PRODUCTS").child(pid);

        binding.pradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,macost,micost,old,olnd;
                name = binding.prname.getText().toString();
                macost=binding.maxcost.getText().toString();
                micost = binding.mincost.getText().toString();
                old = binding.psdes.getText().toString();
                olnd = binding.ldes.getText().toString();
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url=uri.toString();

                                MyModel myModel = new MyModel(name,macost,micost,old,olnd,url,pid);
                                databaseReference.setValue(myModel);
                                Toast.makeText(getContext(), "PRODUCT ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getActivity(),SellerPage.class));
                            }
                        }) ;
                    }
                });

                            }
        });




        return v;


    }

}