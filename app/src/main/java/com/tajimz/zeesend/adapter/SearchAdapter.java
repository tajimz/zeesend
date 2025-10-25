package com.tajimz.zeesend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tajimz.zeesend.databinding.LayoutRPeopleBinding;
import com.tajimz.zeesend.helper.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderSearch> {
    Context context;
    JSONArray jsonArray;
    JSONObject jsonObject;

    public SearchAdapter(Context context, JSONArray jsonArray){
        this.context = context;
        this.jsonArray = jsonArray;


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

            holder.binding.tvName.setText(name);
            holder.binding.tvUsername.setText(username);
            Picasso.get().load(image).into(holder.binding.profileImage);





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
