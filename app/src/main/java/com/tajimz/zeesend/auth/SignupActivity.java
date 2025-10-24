package com.tajimz.zeesend.auth;


import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tajimz.zeesend.R;
import com.tajimz.zeesend.databinding.ActivitySignupBinding;
import com.tajimz.zeesend.helper.BaseActivity;
import com.tajimz.zeesend.helper.CONSTANTS;

import androidx.credentials.CredentialManager;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.Credential;
import androidx.credentials.exceptions.GetCredentialException;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends BaseActivity {
    private ActivitySignupBinding binding;
    private String name, email, pass, conPass;

    FirebaseAuth firebaseAuth;
    CredentialManager credentialManager;
    GetSignInWithGoogleOption googleOption;
    GetCredentialRequest getCredentialRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdge();
        handleGoogle();






    }
    private void setupEdge(){
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(this);
        googleOption = new GetSignInWithGoogleOption.Builder(getString(R.string.googleId)).build();
        getCredentialRequest = new GetCredentialRequest.Builder().addCredentialOption(googleOption).build();

    }

    private boolean isValidInput(){

        name = gettext(binding.edName);
        email = gettext(binding.edMail);
        pass = gettext(binding.edPass);
        conPass = gettext(binding.edConPass);

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()|| conPass.isEmpty()){
            toast("Input all fields");
            return false;

        }

        if (name.length() < 3 || name.length() > 50){
            toast("Enter a valid name (3–50 characters)");
            return false;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            toast("Enter a valid email");
            return false;
        }

        if (!pass.equals(conPass)){
            toast("Password not matched");
            return false;
        }
        if (!isPass(pass)){
            toast(CONSTANTS.passCriteria);
            return false;
        }

        String verified = binding.tvVerify.getText().toString().trim();
        if (!"verified".equals(verified)) {
            toast("Please verify your email");
            return false;
        }

        if (!binding.chkBox.isChecked()){
            toast("You must agree to our privacy policy to continue");
            return false;
        }

        return true;






    }

    private void handleGoogle(){
        binding.tvVerify.setOnClickListener(v->{
            credentialManager.getCredentialAsync(this, getCredentialRequest, null, Runnable::run, new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                @Override
                public void onResult(GetCredentialResponse getCredentialResponse) {

                    Credential credential = getCredentialResponse.getCredential();
                    GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());
                    Log.d("tustus","heree");
                    String email = googleIdTokenCredential.getId();
                    Log.d("tustus",email);


                }

                @Override
                public void onError(@NonNull GetCredentialException e) {
                    Log.d("tustus",e.getMessage().toString());
                }
            });
        });
    }








}