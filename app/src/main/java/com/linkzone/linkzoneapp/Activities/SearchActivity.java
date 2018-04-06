package com.linkzone.linkzoneapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.SearchData;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements ApiResponse {

    ArrayList<SearchData> searchList;
    SearchAdapter searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setHasFixedSize(true);
        searchList = new ArrayList<>();
        searchAdapter = new SearchAdapter(SearchActivity.this, searchList);
        recyclerView.setAdapter(searchAdapter);

        final MyEditText autoCompleteTv = (MyEditText) findViewById(R.id.autoCompleteTv);
        autoCompleteTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchList.clear();
                searchAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchList.clear();
                searchAdapter.notifyDataSetChanged();

                if (s.length() > 0) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("search_text", s);
                        new ApiListener(SearchActivity.this, ApiUrl.UserSearch, jsonObject).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    @Override
    public void onApiResponse(String response) {

        searchList.clear();

        Log.d("Search Response", response);
        try {
            JSONObject jObj = new JSONObject(response);
            if (jObj.getString("response").equals("success")) {

                JSONArray jsonArray = jObj.getJSONArray("searchResult");
                for (int i = 0; i < jsonArray.length(); i++) {
                    SearchData searchData = new SearchData();
                    JSONObject jOb = jsonArray.getJSONObject(i);
                    searchData.setName(jOb.getString("user_name"));
                    //searchData.setEmail(jOb.getString("user_email"));

                    searchList.add(searchData);
                }

            }
            searchAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {

        ArrayList<SearchData> searchList;
        Context context;

        private SearchAdapter(Context context, ArrayList<SearchData> searchList) {
            this.searchList = searchList;
            this.context = context;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_complete_layout, parent, false);

            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {

            SearchData searchData = searchList.get(position);
            holder.userName.setText(searchData.getName());

        }


        @Override
        public int getItemCount() {
            return searchList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView userName;

            public MyHolder(View itemView) {
                super(itemView);
                userName = (TextView) itemView.findViewById(R.id.userName);

                userName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,FinalSearchLayout.class);
                        intent.putExtra("SearchValue",searchList.get(getAdapterPosition()).getName());
                        context.startActivity(intent);
                        finish();
                    }
                });
            }
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
