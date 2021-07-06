package com.example.sny;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sny.databinding.FragmentSellerProductsAddBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.Random;


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


        return v;


    }

}