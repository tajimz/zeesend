package com.tajimz.zeesend.tab;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.tajimz.zeesend.adapter.SearchAdapter;
import com.tajimz.zeesend.databinding.FragmentPeopleBinding;
import com.tajimz.zeesend.helper.BaseFragment;
import com.tajimz.zeesend.helper.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PeopleFragment extends BaseFragment {
    FragmentPeopleBinding binding;
    SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPeopleBinding.inflate(inflater, container, false);

        handleSearchBehavior();
        handleSearch();







        return binding.getRoot();
    }

    private void handleSearchBehavior(){
        binding.srcView.clearFocus();
        binding.srcView.setVisibility(INVISIBLE);
        binding.tool.setVisibility(VISIBLE);

        binding.imgSrc.setOnClickListener(v->{

            binding.srcView.setVisibility(VISIBLE);
            binding.tool.setVisibility(INVISIBLE);
            binding.srcView.setIconified(false);
            binding.srcView.post(() -> {
                binding.srcView.requestFocus();

                // Show keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.srcView.findFocus(), InputMethodManager.SHOW_IMPLICIT);
            });
        });

        binding.srcView.setOnCloseListener(() -> {
            binding.srcView.setVisibility(INVISIBLE);
            binding.tool.setVisibility(VISIBLE);


            return true;
        });
    }

    private void handleSearch(){
        binding.srcView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 3){
                    searchPeople(newText);
                }

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }
        });

    }
    private void searchPeople(String text){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(CONSTANTS.username, getSharedPref(CONSTANTS.username));
            jsonObject.put("searchTerm", text.trim());
            jsonArray.put(jsonObject);

            requestArray(true, CONSTANTS.appUrl + "others/search.php", jsonArray, new ArrayListener() {
                @Override
                public void onSuccess(JSONArray result) {
                    Log.d("tustus", result.toString());
                    handleRecycler(result);




                }
            });





        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    private void handleRecycler(JSONArray jsonArray){
        if (searchAdapter == null) {
            binding.recyclerPeople.setLayoutManager(new LinearLayoutManager(getContext()));
            searchAdapter = new SearchAdapter(getContext(), jsonArray);
            binding.recyclerPeople.setAdapter(searchAdapter);
        } else {
            searchAdapter.updateData(jsonArray);
        }

    }


}