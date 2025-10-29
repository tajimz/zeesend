package com.tajimz.zeesend.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tajimz.zeesend.ChatActivity;
import com.tajimz.zeesend.ContainerActivity;
import com.tajimz.zeesend.databinding.LayoutRPeopleBinding;
import com.tajimz.zeesend.helper.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderSearch> {
    Context context;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Boolean directChat;

    public SearchAdapter(Context context, JSONArray jsonArray, Boolean directChat){
        this.context = context;
        this.jsonArray = jsonArray;
        this.directChat = directChat;


    }

    public class ViewHolderSearch extends RecyclerView.ViewHolder{
        LayoutRPeopleBinding binding;

        public ViewHolderSearch(LayoutRPeopleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public ViewHolderSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutRPeopleBinding binding = LayoutRPeopleBinding.inflate(inflater, parent, false);

        return new ViewHolderSearch(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSearch holder, int position) {
        jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) jsonArray.get(position);
            String name = jsonObject.getString(CONSTANTS.name);
            String username = jsonObject.getString(CONSTANTS.username);
            String email = jsonObject.getString(CONSTANTS.email);
            String bio = jsonObject.getString(CONSTANTS.bio);
            String image = jsonObject.getString(CONSTANTS.image);
            String id = jsonObject.getString(CONSTANTS.id);
            String date = jsonObject.getString(CONSTANTS.createTime);

            holder.binding.tvName.setText(name);
            holder.binding.tvUsername.setText(username);
            Picasso.get().load(image).into(holder.binding.profileImage);

            holder.binding.main.setOnClickListener(v->{
                Intent intent;
                if (! directChat) intent = new Intent(context, ContainerActivity.class);
                else intent = new Intent(context, ChatActivity.class);
                intent.putExtra("status", "profile");
                intent.putExtra(CONSTANTS.name, name);
                intent.putExtra(CONSTANTS.username, username);
                intent.putExtra(CONSTANTS.email, email);
                intent.putExtra(CONSTANTS.bio, bio);
                intent.putExtra(CONSTANTS.image, image);
                intent.putExtra(CONSTANTS.id, id);
                intent.putExtra(CONSTANTS.id, id);
                intent.putExtra(CONSTANTS.createTime, date);
                context.startActivity(intent);

            });







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
