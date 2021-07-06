package com.example.sny;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class SellerPage extends AppCompatActivity {

    TabLayout tl;
    ViewPager vp;


    ArrayList<Fragment> fragmentsArraylist = new ArrayList<>();
    ArrayList<String> fragmentTitle = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_page);

        tl=findViewById(R.id.tl);
        vp = findViewById(R.id.vp);
        tl.setupWithViewPager(vp);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Seller_Products_List(),"PRODUCTS");
        vpAdapter.addFragment(new Seller_Products_Add(),"ADD PRODUCTS");
        vpAdapter.addFragment(new Seller_Products_Stat(),"STATISTICS");
        vp.setAdapter(vpAdapter);


    }

    public class VPAdapter extends FragmentPagerAdapter{

        public VPAdapter(@NonNull  FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragmentsArraylist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsArraylist.size();
        }

        public void addFragment(Fragment fragment,String title)
        {
            fragmentsArraylist.add(fragment);
            fragmentTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

}