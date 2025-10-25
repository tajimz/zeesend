package com.tajimz.zeesend.tab;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
    private String name, username, bio, email, image, id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupTextViews();


        return binding.getRoot();
    }

    private void setupTextViews(){
        if (getArguments() != null) {
            name = getArguments().getString(CONSTANTS.name);
            username = getArguments().getString(CONSTANTS.username);
            bio = getArguments().getString(CONSTANTS.bio);
            email = getArguments().getString(CONSTANTS.email);
            image = getArguments().getString(CONSTANTS.image);
            id = getArguments().getString(CONSTANTS.id);
            binding.btnSend.setVisibility(VISIBLE);
        } else {
            name = getSharedPref(CONSTANTS.name);
            username = getSharedPref(CONSTANTS.username);
            bio = getSharedPref(CONSTANTS.bio);
            email = getSharedPref(CONSTANTS.email);
            image = getSharedPref(CONSTANTS.image);
            binding.btnSend.setVisibility(GONE);
        }

        binding.tvName.setText(name);
        binding.tvUsername.setText(username);
        binding.tvBio.setText(bio);
        binding.tvEmail.setText(email);
        Picasso.get().load(image).into(binding.image);

    }
}