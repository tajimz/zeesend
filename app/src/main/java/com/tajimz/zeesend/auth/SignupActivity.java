package com.tajimz.zeesend.auth;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tajimz.zeesend.MainActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

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
        setupEdgeToEdge();
        setupEssentials();
        handleCreate();
        handleGoogle();
        handleLinks();






    }
    private void setupEssentials(){
        firebaseAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(this);
        googleOption = new GetSignInWithGoogleOption.Builder(getString(R.string.googleId)).build();
        getCredentialRequest = new GetCredentialRequest.Builder().addCredentialOption(googleOption).build();

    }


    private void handleCreate(){
        binding.btnCreate.setOnClickListener(v->{


            if (!isValidInput()){
                return;
            }


            JSONObject jsonObject = new JSONObject();

            String username = generateUniqueId(this);
            putInJsonObj(jsonObject, CONSTANTS.name, name);
            putInJsonObj(jsonObject, CONSTANTS.email, email);
            putInJsonObj(jsonObject ,CONSTANTS.password, pass);
            putInJsonObj(jsonObject, CONSTANTS.username, username);

            Log.d("tustus", jsonObject.toString());

            requestObj(false, CONSTANTS.appUrl + "auth/signup.php", jsonObject, new ObjListener() {
                @Override
                public void onSuccess(JSONObject result) {

                    String status = getStrFromJsonObj(result ,"status");
                    if ("done".equals(status)) {
                        String id = getStrFromJsonObj(result, CONSTANTS.id);
                        doneSignup(name, email, username, id, getCurrentDate());
                    }
                    else {
                        toast(status);
                    }

                }
            });









        });
    }
    private void handleGoogle(){
        binding.tvVerify.setOnClickListener(v->{
            email = gettext(binding.edMail);
            if (email.isEmpty() || "verified".equals(binding.tvVerify.getText().toString())) return;





            credentialManager.getCredentialAsync(this, getCredentialRequest, null, Runnable::run, new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                @Override
                public void onResult(GetCredentialResponse getCredentialResponse) {

                    Credential credential = getCredentialResponse.getCredential();
                    GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());
                    String emailG = googleIdTokenCredential.getId();
                    runOnUiThread(()->{
                        if (email.equals(emailG)) binding.tvVerify.setText("verified");
                        else toast("Please select the email you have entered");
                    });




                }

                @Override
                public void onError(@NonNull GetCredentialException e) {
                    Log.d("tustus",e.getMessage().toString());
                }
            });
        });
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

    private void handleLinks(){
        //redirect to privacy policy

        binding.tvLogin.setOnClickListener(v->{
            startActivity(new Intent(this, LoginActivity.class));
        });
    }


    private void doneSignup(String name, String email, String username, String id, String date){
        String bio = CONSTANTS.defaultBio;
        String image = CONSTANTS.defaultImage;

        editSharedPref(CONSTANTS.bio, bio);
        editSharedPref(CONSTANTS.image, image);
        editSharedPref(CONSTANTS.name, name);
        editSharedPref(CONSTANTS.email, email);
        editSharedPref(CONSTANTS.username, username);
        editSharedPref(CONSTANTS.id, id);
        editSharedPref(CONSTANTS.createTime, date);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);





    }









}