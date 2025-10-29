package com.tajimz.zeesend.tab;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tajimz.zeesend.ChatActivity;
import com.tajimz.zeesend.R;
import com.tajimz.zeesend.databinding.FragmentProfileBinding;
import com.tajimz.zeesend.helper.BaseFragment;
import com.tajimz.zeesend.helper.CONSTANTS;


public class ProfileFragment extends BaseFragment {
    FragmentProfileBinding binding;
    private String name, username, bio, email, image, id, createTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupTextViews();
        setupClickListeners();
        handleMenu(binding.imgMore);


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
            createTime = getArguments().getString(CONSTANTS.createTime);
            binding.btnSend.setVisibility(VISIBLE);
        } else {
            name = getSharedPref(CONSTANTS.name);
            username = getSharedPref(CONSTANTS.username);
            bio = getSharedPref(CONSTANTS.bio);
            email = getSharedPref(CONSTANTS.email);
            image = getSharedPref(CONSTANTS.image);
            createTime = getSharedPref(CONSTANTS.createTime);
            binding.btnSend.setVisibility(GONE);
        }

        binding.tvName.setText(name);
        binding.tvUsername.setText(username);
        binding.tvBio.setText(bio);
        binding.tvEmail.setText(email);
        binding.tvDate.setText(createTime);
        Picasso.get().load(image).into(binding.image);

    }

    private void setupClickListeners(){
        binding.btnSend.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra(CONSTANTS.name, name);
            intent.putExtra(CONSTANTS.id, id);
            intent.putExtra(CONSTANTS.username, username);
            intent.putExtra(CONSTANTS.bio, bio);
            intent.putExtra(CONSTANTS.email, email);
            intent.putExtra(CONSTANTS.image, image);
            intent.putExtra(CONSTANTS.createTime, createTime);
            startActivity(intent);
        });
    }
}