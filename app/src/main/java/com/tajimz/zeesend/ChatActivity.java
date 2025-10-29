package com.tajimz.zeesend;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;
import com.tajimz.zeesend.adapter.ChatAdapter;
import com.tajimz.zeesend.databinding.ActivityChatBinding;
import com.tajimz.zeesend.helper.BaseActivity;
import com.tajimz.zeesend.helper.CONSTANTS;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatActivity extends BaseActivity {
    ActivityChatBinding binding;
    String id, name, image, currentUserId, roomId, bio, username, date, email;
    ChatAdapter chatAdapter;
    String lastMessage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdge();
        setupTexts();
        findRoom();
        handleSend();
        startLooping();
        handleClicks();
        handleMenu();




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
        bio = getIntent().getStringExtra(CONSTANTS.bio);
        username = getIntent().getStringExtra(CONSTANTS.username);
        date = getIntent().getStringExtra(CONSTANTS.createTime);
        email = getIntent().getStringExtra(CONSTANTS.email);

        binding.tvName.setText(name);
        Picasso.get().load(image).into(binding.imgProfile);
    }

    private void findRoom(){
        currentUserId = getSharedPref(CONSTANTS.id);
        JSONObject jsonObject = new JSONObject();

        if (Integer.parseInt(id) > Integer.parseInt(currentUserId)){
            putInJsonObj(jsonObject, "user1", currentUserId);
            putInJsonObj(jsonObject, "user2", id);

        }else {
            putInJsonObj(jsonObject, "user1", id);
            putInJsonObj(jsonObject, "user2", currentUserId );
        }



        requestObj(true, CONSTANTS.appUrl + "chats/findRoom.php", jsonObject, new ObjListener() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("tustus", result.toString());
                roomId = getStrFromJsonObj(result, "room_id");
                getMessagesAll(false, roomId);


            }
        });


    }



    private void handleRecycler(JSONArray jsonArray, String currentId){
        if (chatAdapter == null) {
            binding.recyclerChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
            chatAdapter = new ChatAdapter(ChatActivity.this, currentId, jsonArray);
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

            putInJsonObj(jsonObject, "sender_id", currentUserId);
            putInJsonObj(jsonObject, "room_id", roomId);
            putInJsonObj(jsonObject, "message", text.trim());

            binding.tvSend.setText("Sending : "+text);
            binding.tvSend.setVisibility(VISIBLE);
            binding.edMessage.setText("");

            requestObj(true, CONSTANTS.appUrl + "chats/sendMessage.php", jsonObject, new ObjListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    Log.d("tustus", result.toString());
                    binding.tvSend.setVisibility(GONE);
                    getMessagesAll(true, roomId);

                }
            });

        });

        binding.imgMore.setOnClickListener(v->{
            getMessagesAll(true, roomId);
        });
    }

    private void getMessagesAll(Boolean onlyNew, String roomId){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        putInJsonObj(jsonObject, "room_id", roomId);
        putInJsonObj(jsonObject, "last_updated", lastMessage);

        if (onlyNew) putInJsonObj(jsonObject, "only_new", "1");
        else putInJsonObj(jsonObject, "only_new", "0");

        jsonArray.put(jsonObject);

        requestArray(true, CONSTANTS.appUrl + "chats/getMessages.php", jsonArray, new ArrayListener() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.d("tustus", result.toString());
                if (onlyNew) chatAdapter.addData(result);
                else handleRecycler(result, currentUserId);
                lastMessage = getLastMessageTime(result, lastMessage) ;
                binding.recyclerChat.scrollToPosition(chatAdapter.getItemCount() - 1);


            }
        });



    }

    private Handler handler = new Handler();
    private Runnable runnable;

    private void startLooping() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (roomId != null) {
                    getMessagesAll(true, roomId); // call your method
                }
                handler.postDelayed(this, 3000); // repeat every 5 seconds
            }
        };
        handler.post(runnable);
    }

    private void handleClicks(){
        binding.imgBack.setOnClickListener(v->{
            super.onBackPressed();
        });
    }

    private void handleMenu(){
        binding.imgMore.setOnClickListener(v->{
            PopupMenu menu = new PopupMenu(this, v);
            menu.getMenuInflater().inflate(R.menu.three_dots_chat, menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.delete_chat){
                        toast("Coming soon ...");
                    }else if (id == R.id.block_usr){
                        toast("Coming soon ...");
                    }
                    return false;
                }
            });
            menu.show();
        });



        binding.tvName.setOnClickListener(v->{
            Intent intent = new Intent(this, ContainerActivity.class);
            intent.putExtra("status", "profile");
            intent.putExtra(CONSTANTS.name, name);
            intent.putExtra(CONSTANTS.username, username);
            intent.putExtra(CONSTANTS.email, email);
            intent.putExtra(CONSTANTS.bio, bio);
            intent.putExtra(CONSTANTS.image, image);
            intent.putExtra(CONSTANTS.id, id);
            intent.putExtra(CONSTANTS.id, id);
            intent.putExtra(CONSTANTS.createTime, date);
            startActivity(intent);
            finish();
        });
    }









    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable); // stop looping when activity is destroyed
        }
    }
}