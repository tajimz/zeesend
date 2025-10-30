package com.tajimz.zeesend.tab;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.tajimz.zeesend.R;
import com.tajimz.zeesend.adapter.SearchAdapter;
import com.tajimz.zeesend.databinding.FragmentChatBinding;
import com.tajimz.zeesend.helper.BaseFragment;
import com.tajimz.zeesend.helper.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONObject;


public class ChatFragment extends BaseFragment {
    FragmentChatBinding binding;
    SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        getChatLists();
        handleMenu(binding.imgMore);
        startLooping();

        return binding.getRoot();
    }

    private void getChatLists(){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        putInJsonObj(jsonObject, CONSTANTS.id, getSharedPref(CONSTANTS.id));
        jsonArray.put(jsonObject);

        Log.d("tustus", jsonArray.toString());

        requestArray(true, CONSTANTS.appUrl + "chats/getLists.php", jsonArray, new ArrayListener() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.d("tustusRR", result.toString());
                handleRecycler(result, true);



            }
        });
    }

    private void handleRecycler(JSONArray jsonArray, Boolean directChat){
        if (searchAdapter == null) {
            binding.recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));
            searchAdapter = new SearchAdapter(getContext(), jsonArray, directChat);
            binding.recyclerChat.setAdapter(searchAdapter);
        } else {
            searchAdapter.updateData(jsonArray);
        }

    }

    private Handler handler = new Handler();
    private Runnable runnable;

    private void startLooping() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (searchAdapter != null) {
                    getChatLists(); // call your method
                }
                handler.postDelayed(this, 2000); // repeat every 5 seconds
            }
        };
        handler.post(runnable);
    }



}