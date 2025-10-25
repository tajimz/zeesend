package com.tajimz.zeesend.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tajimz.zeesend.tab.ChatFragment;
import com.tajimz.zeesend.tab.PeopleFragment;
import com.tajimz.zeesend.tab.ProfileFragment;

public class ViewPagerII extends FragmentStateAdapter {
    public ViewPagerII(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ChatFragment();
            case 1:
                return new PeopleFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new ChatFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
