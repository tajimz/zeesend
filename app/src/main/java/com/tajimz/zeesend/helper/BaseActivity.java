package com.tajimz.zeesend.helper;


import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tajimz.zeesend.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity {

    public interface ObjListener{
        void onSuccess(JSONObject result);
    }
    public interface ArrayListener{
        void onSuccess(JSONArray result);
    }

    protected void requestObj(Boolean silent, String url, JSONObject jsonObject, ObjListener objListener){
        if (!silent) startLoading();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                endLoading();
                objListener.onSuccess(jsonObject);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                endLoading();
                errorAlert("Server Error "+volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    protected void requestArray(Boolean silent , String url, JSONArray jsonArray, ArrayListener arrayListener){
        if (!silent) startLoading();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                arrayListener.onSuccess(jsonArray);
                endLoading();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                errorAlert("Server Error "+volleyError.toString());
                endLoading();
            }
        });
        requestQueue.add(jsonArrayRequest);


    }

    protected void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected String gettext(EditText editText){
        return editText.getText().toString().trim();
    }

    protected boolean isPass(String string ){
        return string.length() >= 8 && string.length() <=32;
    }






    //setup loading dialog

    private AlertDialog loadingDialog;
    protected void startLoading() {
        if (loadingDialog == null) {
            loadingDialog = new AlertDialog.Builder(this)
                    .setView(LayoutInflater.from(this).inflate(R.layout.alert_loading, null))
                    .setCancelable(false)
                    .create();
            if (loadingDialog.getWindow() != null)
                loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        loadingDialog.show();
    }

    protected void endLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    protected void errorAlert(String message){
        new AlertDialog.Builder(this).setTitle("Error occurred").setMessage(message).setNeutralButton("Understand", null).show();

    }


}
