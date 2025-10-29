package com.tajimz.zeesend.tab;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.tajimz.zeesend.ChatActivity;
import com.tajimz.zeesend.R;
import com.tajimz.zeesend.databinding.FragmentProfileBinding;
import com.tajimz.zeesend.helper.BaseFragment;
import com.tajimz.zeesend.helper.CONSTANTS;

import org.json.JSONObject;


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
        name = getValue(CONSTANTS.name);
        username = getValue(CONSTANTS.username);
        bio = getValue(CONSTANTS.bio);
        email = getValue(CONSTANTS.email);
        image = getValue(CONSTANTS.image);
        id = getValue(CONSTANTS.id);
        createTime = getValue(CONSTANTS.createTime);
        handleCause();

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

    private void handleEdit(){
        binding.nameRl.setOnClickListener(v->{
            editOption(CONSTANTS.name);

        });
        binding.aboutRl.setOnClickListener(v->{
            editOption(CONSTANTS.bio);
        });

        binding.rlUsername.setOnClickListener(v->{
            editOption(CONSTANTS.username);
        });
        binding.image.setOnClickListener(v->{

        });
    }


    private void editOption(String why){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.layout_edit, null);
        bottomSheetDialog.setContentView(sheetView);

        TextView tvTitle = sheetView.findViewById(R.id.tvEdit);
        EditText editText = sheetView.findViewById(R.id.edEdit);
        MaterialButton button = sheetView.findViewById(R.id.btnEdit);

        tvTitle.setText("Change "+why+" :");
        button.setOnClickListener(v->{
            String text = editText.getText().toString().trim();
            if (text.isEmpty()) return;

            if (CONSTANTS.name.equals(why) && (text.length() < 3 || text.length() > 50)) return;
            else if (CONSTANTS.bio.equals(why) && (text.length() < 5 || text.length() > 50)) return;
            else if (CONSTANTS.username.equals(why) && (text.length() < 4 || text.length() > 16)) return;

            editInfo(why, text);
            bottomSheetDialog.dismiss();

        });

        bottomSheetDialog.show();





    }
    private void editInfo(String reason, String text){

        JSONObject jsonObject = new JSONObject();
        putInJsonObj(jsonObject, "reason", reason);
        putInJsonObj(jsonObject, "text", text);
        putInJsonObj(jsonObject, CONSTANTS.id, getSharedPref(CONSTANTS.id));
        Log.d("tustus",jsonObject.toString());


        requestObj(false, CONSTANTS.appUrl + "others/edit.php", jsonObject, new ObjListener() {
            @Override
            public void onSuccess(JSONObject result) {
                String status = getStrFromJsonObj(result, "status");
                if ("done".equals(status)) {
                    editSharedPref(reason, text);
                    toast("Successfully changed");
                    setupTextViews();
                }else {
                    toast(status);
                }

            }
        });


    }

    private String getValue(String key) {
        if (getArguments() != null) return getArguments().getString(key);
        return getSharedPref(key);
    }

    private void handleCause(){
        if (getArguments() != null){
            binding.btnSend.setVisibility(VISIBLE);
        }else {
            binding.btnSend.setVisibility(GONE);
            handleEdit();
        }
    }
}