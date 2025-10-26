package com.tajimz.zeesend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajimz.zeesend.databinding.LayoutChatBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolderChat> {
    Context context;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String userId;

    public ChatAdapter(Context context, String userId, JSONArray jsonArray){
        this.context = context;
        this.jsonArray = jsonArray;
        this.userId = userId;
    }

    public class ViewHolderChat extends RecyclerView.ViewHolder {
        LayoutChatBinding binding;

        public ViewHolderChat(LayoutChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutChatBinding binding = LayoutChatBinding.inflate(inflater, parent, false);
        return new ViewHolderChat(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChat holder, int position) {
        jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) jsonArray.get(position);
            String message = jsonObject.getString("message");
            String senderId = jsonObject.getString("senderId");

            if (userId.equals(senderId)) {
                holder.binding.tvMe.setText(message);
            } else {
                holder.binding.tvHe.setText(message);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public void updateData(JSONArray newArray) {
        this.jsonArray = newArray;
        notifyDataSetChanged();
    }
}
