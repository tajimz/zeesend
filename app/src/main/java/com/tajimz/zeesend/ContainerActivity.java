package com.tajimz.zeesend;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tajimz.zeesend.databinding.ActivityContainerBinding;
import com.tajimz.zeesend.helper.BaseActivity;
import com.tajimz.zeesend.helper.CONSTANTS;
import com.tajimz.zeesend.tab.ProfileFragment;

public class ContainerActivity extends BaseActivity {
    ActivityContainerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdge();
        String status = getIntent().getStringExtra("status");
        if ("profile".equals(status)) handleProfile();
    }

    private void setupEdge(){
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void handleProfile(){

        Bundle bundle = new Bundle();
        bundle.putString(CONSTANTS.name, getIntent().getStringExtra(CONSTANTS.name));
        bundle.putString(CONSTANTS.bio, getIntent().getStringExtra(CONSTANTS.bio));
        bundle.putString(CONSTANTS.email, getIntent().getStringExtra(CONSTANTS.email));
        bundle.putString(CONSTANTS.image, getIntent().getStringExtra(CONSTANTS.image));
        bundle.putString(CONSTANTS.username, getIntent().getStringExtra(CONSTANTS.username));
        bundle.putString(CONSTANTS.id, getIntent().getStringExtra(CONSTANTS.id));
        bundle.putString(CONSTANTS.createTime, cleanDate(getIntent().getStringExtra(CONSTANTS.createTime)));

        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, profileFragment)
                .commit();


    }

}