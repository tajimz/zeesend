package com.tajimz.zeesend.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

public class BaseFragment extends Fragment {

    protected String getSharedPref(String keyword) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(CONSTANTS.SHAREDPREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyword, null);
    }

    protected void editSharedPref(String keyword, String value) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(CONSTANTS.SHAREDPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyword, value);
        editor.apply();
    }

    public interface ObjListener{
        void onSuccess(JSONObject result);
    }
    public interface ArrayListener{
        void onSuccess(JSONArray result);
    }

    protected void requestObj(Boolean silent, String url, JSONObject jsonObject, ObjListener objListener){
        if (!silent) startLoading();
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
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
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
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
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
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
            loadingDialog = new AlertDialog.Builder(requireContext())
                    .setView(LayoutInflater.from(requireContext()).inflate(R.layout.alert_loading, null))
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
        new AlertDialog.Builder(requireContext()).setTitle("Error occurred").setMessage(message).setNeutralButton("Understand", null).show();
    }

    public static String generateUniqueId(Context context) {
        String androidId = android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID
        );

        long time = System.currentTimeMillis();

        String base = androidId + time;

        long hash = Math.abs(base.hashCode());

        return String.format("%010d", hash % 1_000_000_0000L);
    }
}
