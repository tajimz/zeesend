package com.tajimz.zeesend.tab;

import static android.view.View.GONE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tajimz.zeesend.R;
import com.tajimz.zeesend.databinding.FragmentProfileBinding;
import com.tajimz.zeesend.helper.BaseFragment;
import com.tajimz.zeesend.helper.CONSTANTS;


public class ProfileFragment extends BaseFragment {
    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupTextViews();


        return binding.getRoot();
    }

    private void setupTextViews(){
        String name = getSharedPref(CONSTANTS.name);
        String username = getSharedPref(CONSTANTS.username);
        String bio = getSharedPref(CONSTANTS.bio);
        String email = getSharedPref(CONSTANTS.email);
        String image = getSharedPref(CONSTANTS.image);

        binding.tvName.setText(name);
        binding.tvUsername.setText(username);
        binding.tvBio.setText(bio);
        binding.tvEmail.setText(email);
        Picasso.get().load(image).into(binding.image);
        binding.btnSend.setVisibility(GONE);
    }
}