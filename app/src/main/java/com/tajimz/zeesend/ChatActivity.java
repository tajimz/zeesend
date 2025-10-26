package com.tajimz.zeesend;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;
import com.tajimz.zeesend.adapter.ChatAdapter;
import com.tajimz.zeesend.adapter.SearchAdapter;
import com.tajimz.zeesend.databinding.ActivityChatBinding;
import com.tajimz.zeesend.helper.BaseActivity;
import com.tajimz.zeesend.helper.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends BaseActivity {
    ActivityChatBinding binding;
    String id, name, image, currentUserId, roomId;
    ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdge();
        setupTexts();
        findRoom();
        handleSend();



    }

    private void setupEdge(){
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.tvSend.setVisibility(GONE);

    }

    private void setupTexts(){
        id = getIntent().getStringExtra(CONSTANTS.id);
        name = getIntent().getStringExtra(CONSTANTS.name);
        image = getIntent().getStringExtra(CONSTANTS.image);

        binding.tvName.setText(name);
        Picasso.get().load(image).into(binding.imgProfile);
    }

    private void findRoom(){
        currentUserId = getSharedPref(CONSTANTS.id);

        JSONObject jsonObject = new JSONObject();
        try {
        if (Integer.parseInt(id) > Integer.parseInt(currentUserId)){

                jsonObject.put("user1", currentUserId);
                jsonObject.put("user2", id);

        }else {
            jsonObject.put("user1", id);
            jsonObject.put("user2", currentUserId );
        }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        requestObj(true, CONSTANTS.appUrl + "chats/findRoom.php", jsonObject, new ObjListener() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                     roomId = result.getString("room_id");
                    getMessages(roomId);
                    Log.d("tustus", roomId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    private void getMessages(String roomId){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("room_id", roomId);
            jsonArray.put(jsonObject);

            requestArray(true, CONSTANTS.appUrl + "chats/getMessages.php", jsonArray, new ArrayListener() {
                @Override
                public void onSuccess(JSONArray result) {
                    Log.d("tustus", result.toString());
                    handleRecycler(result, currentUserId);


                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    private void handleRecycler(JSONArray jsonArray, String currentId){
        if (chatAdapter == null) {
            binding.recyclerChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            chatAdapter = new ChatAdapter(getApplicationContext(), currentId, jsonArray);
            binding.recyclerChat.setAdapter(chatAdapter);
        } else {
            chatAdapter.updateData(jsonArray);
        }

    }

    private void handleSend(){
        binding.btnSend.setOnClickListener(v->{
            String text = gettext(binding.edMessage);
            if (text.isEmpty()) return;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sender_id", currentUserId);
                jsonObject.put("room_id", roomId);
                jsonObject.put("message", text);

                binding.tvSend.setText("Sending : "+text);
                binding.tvSend.setVisibility(VISIBLE);
                binding.edMessage.setText("");
                requestObj(true, CONSTANTS.appUrl + "chats/sendMessage.php", jsonObject, new ObjListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.d("tustus", result.toString());
                        binding.tvSend.setVisibility(GONE);
                        getMessages(roomId);

                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        binding.imgMore.setOnClickListener(v->{
            getMessages(roomId);
        });
    }
}