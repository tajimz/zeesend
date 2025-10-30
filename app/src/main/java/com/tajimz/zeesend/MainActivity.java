package com.tajimz.zeesend;


import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.tajimz.zeesend.databinding.ActivityMainBinding;
import com.tajimz.zeesend.helper.BaseActivity;
import com.tajimz.zeesend.helper.ViewPagerII;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    ViewPagerII viewPagerII;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdgeToEdge();
        setupTabs();

    }


    private void setupTabs(){
        viewPagerII = new ViewPagerII(this);
        binding.pager2.setAdapter(viewPagerII);
        binding.tabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.pager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.pager2.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLay.getTabAt(position).select();
            }
        });

    }
}