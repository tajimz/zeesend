package com.tajimz.zeesend;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

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
        setupEdgeToEdge();
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateNext, 2000);



    }


    private void navigateNext(){
        String name = getSharedPref(CONSTANTS.name);
        if (name != null) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class)); finish();
        }else {
            startActivity(new Intent(SplashActivity.this, SignupActivity.class)); finish();
        }
    }
}