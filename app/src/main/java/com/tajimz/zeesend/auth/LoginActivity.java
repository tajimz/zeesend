package com.tajimz.zeesend.auth;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.tajimz.zeesend.MainActivity;
import com.tajimz.zeesend.R;
import com.tajimz.zeesend.databinding.ActivityLoginBinding;
import com.tajimz.zeesend.helper.BaseActivity;
import androidx.credentials.CredentialManager;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.Credential;
import androidx.credentials.exceptions.GetCredentialException;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.tajimz.zeesend.helper.CONSTANTS;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    FirebaseAuth firebaseAuth;
    CredentialManager credentialManager;
    GetSignInWithGoogleOption googleOption;
    GetCredentialRequest getCredentialRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdge();
        handleLinks();
        handleGoogle();
        handleManual();


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

    private void handleLinks(){
        //redirect to privacy policy

        binding.tvSignin.setOnClickListener(v->{
            super.onBackPressed();
        });
    }



    private void handleGoogle(){
        binding.loginGoogle.setOnClickListener(v->{

            credentialManager.getCredentialAsync(this, getCredentialRequest, null, Runnable::run, new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                @Override
                public void onResult(GetCredentialResponse getCredentialResponse) {

                    Credential credential = getCredentialResponse.getCredential();
                    GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());
                    String emailG = googleIdTokenCredential.getId();
                    runOnUiThread(()->{
                        requestServer(emailG, "google", "");
                    });




                }

                @Override
                public void onError(@NonNull GetCredentialException e) {
                    Log.d("tustus",e.getMessage().toString());
                }
            });
        });
    }

    private void requestServer(String email, String via, String pass){
        JSONObject jsonObject = new JSONObject();

        putInJsonObj(jsonObject, CONSTANTS.email, email);
        putInJsonObj(jsonObject, CONSTANTS.password, pass);
        putInJsonObj(jsonObject, "via", via);


        requestObj(false, CONSTANTS.appUrl + "auth/login.php", jsonObject, new ObjListener() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("tustus", result.toString());

                String status = getStrFromJsonObj(result, "status");
                if (!"done".equals(status)){
                    toast(status);
                    return;
                }
                String name = getStrFromJsonObj(result, CONSTANTS.name);
                String email = getStrFromJsonObj(result, CONSTANTS.email);
                String username = getStrFromJsonObj(result, CONSTANTS.username);
                String bio = getStrFromJsonObj(result, CONSTANTS.bio);
                String image = getStrFromJsonObj(result, CONSTANTS.image);
                String id = getStrFromJsonObj(result, CONSTANTS.id);

                doneLogin(name, email, username, bio, image, id);

            }
        });




    }

    private void handleManual(){
        binding.btnLog.setOnClickListener(v -> {
            String email = gettext(binding.edEmail);
            String password = gettext(binding.edPass);
            if (email.isEmpty() || password.isEmpty()) {
                toast("Enter data");
                return;
            }
            requestServer(email, "email", password);
        });
    }

    private void doneLogin(String name, String email, String username , String bio, String image, String id){

        editSharedPref(CONSTANTS.bio, bio);
        editSharedPref(CONSTANTS.image, image);
        editSharedPref(CONSTANTS.name, name);
        editSharedPref(CONSTANTS.email, email);
        editSharedPref(CONSTANTS.username, username);
        editSharedPref(CONSTANTS.id, id);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);





    }
}