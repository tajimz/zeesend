package com.tajimz.zeesend;



import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tajimz.zeesend.auth.SignupActivity;
import com.tajimz.zeesend.databinding.ActivitySplashBinding;
import com.tajimz.zeesend.helper.BaseActivity;
import com.tajimz.zeesend.helper.CONSTANTS;

public class SplashActivity extends BaseActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdge();
        if (!getSharedPref(CONSTANTS.name).isEmpty()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        }


    }
    private void setupEdge(){
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




    }
}