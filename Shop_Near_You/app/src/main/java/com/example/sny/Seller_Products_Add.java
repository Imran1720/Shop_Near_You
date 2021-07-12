package com.example.sny;

import android.app.ProgressDialog;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.sny.databinding.FragmentSellerProductsAddBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class Seller_Products_Add extends Fragment {
    View v;

    //network objects
    FragmentSellerProductsAddBinding fragmentSellerProductsAddBinding;
    DatabaseReference databaseReference,prdref;
    StorageReference storageReference;
    //progress Dialog
    ProgressDialog pd;
    //current user id
    String uid,sname;
    //product photo
    Uri uri;
    //random uuid for photo
    String picid,sid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_seller__products__add, container, false);
        //data binding object
        fragmentSellerProductsAddBinding = DataBindingUtil.setContentView(getActivity(),R.layout.fragment_seller__products__add);

        //network objs
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("SNY_USER_IMAGES/"+picid);
        prdref = FirebaseDatabase.getInstance().getReference().child("SNY").child("PRODUCTS");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sname = snapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //progress dialog
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Please Wait...");
        pd.setProgress(ProgressDialog.STYLE_SPINNER);


        //random uuid for photo
        picid = UUID.randomUUID().toString();

        //select product pic
        ActivityResultLauncher<String> mgetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                uri = result;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(v.getContext().getContentResolver(), uri);
                    fragmentSellerProductsAddBinding.propic.setImageBitmap(bitmap);
                    fragmentSellerProductsAddBinding.propic.setBackground(null);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //open directory to select pic
        fragmentSellerProductsAddBinding.propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgetContent.launch("image/*");
            }
        });

        //add button
        fragmentSellerProductsAddBinding.pradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();
                storageReference.putFile(uri).addOnSuccessListener(getActivity(),new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                MyModel myModel = new MyModel(url,fragmentSellerProductsAddBinding.prname.getText().toString(),
                                        fragmentSellerProductsAddBinding.maxcost.getText().toString(),
                                        fragmentSellerProductsAddBinding.mincost.getText().toString(),
                                        fragmentSellerProductsAddBinding.number.getText().toString(),
                                        fragmentSellerProductsAddBinding.cate.getText().toString(),
                                        fragmentSellerProductsAddBinding.psdes.getText().toString(),
                                        fragmentSellerProductsAddBinding.ldes.getText().toString(),
                                        picid,sname,uid);

                                Thread mThread = new Thread() {
                                    @Override
                                    public void run() {
                                        prdref.child(picid).setValue(myModel);

                                        pd.dismiss();
                                    }
                                };
                                mThread.start();

                                Toast.makeText(getContext(), "PRODUCT WAS ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getActivity(),SellerPage.class));

                            }
                        });
                    }
                });



            }
        });

        fragmentSellerProductsAddBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SellerPage.class));            }
        });
        return v;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}