package com.linkzone.linkzoneapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.linkzone.linkzoneapp.Adapters.SearchAdapters;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.SearchUserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinalSearchLayout extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SearchUserDetails> packageNames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_search_layout);
        APIWork();
        BindPack();
    }

    public void BindPack()
    {
        recyclerView=(RecyclerView)findViewById(R.id.FSearchRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SearchAdapters(FinalSearchLayout.this,packageNames);
        recyclerView.setAdapter(adapter);
    }

    public void APIWork()
    {
        Functions.showLoadingDialog(FinalSearchLayout.this,"Please Wait....");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("search_text",getIntent().getStringExtra("SearchValue"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(FinalSearchLayout.this);
        AndroidNetworking.post(ApiUrl.SearchUserList)
                .setPriority(Priority.MEDIUM)
                .setTag("Token")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Functions.closeLoadingDialog();

                        try {
                            if(response.getString("response").matches("success"))
                            {
                                JSONArray ar = response.getJSONArray("SearchUserList");
                                Log.e("Array",response.getJSONArray("SearchUserList").toString());
                                for(int i=0;i<ar.length();i++)
                                {
                                    SearchUserDetails searchUserDetails = new SearchUserDetails();
                                    searchUserDetails.setUName(ar.getJSONObject(i).getString("user_name"));
                                    searchUserDetails.setUID(ar.getJSONObject(i).getString("id"));
                                    searchUserDetails.setUEmail(ar.getJSONObject(i).getString("user_email"));
                                    searchUserDetails.setUImage(ar.getJSONObject(i).getString("image"));
                                    searchUserDetails.setUMob(ar.getJSONObject(i).getString("user_mobile"));
                                    searchUserDetails.setUDob(ar.getJSONObject(i).getString("user_dob"));
                                    searchUserDetails.setUstatus(ar.getJSONObject(i).getString("timeline_status"));
                                    searchUserDetails.setUinterestin(ar.getJSONObject(i).getString("interested_in"));
                                    searchUserDetails.setUlives(ar.getJSONObject(i).getString("lives_in"));
                                    searchUserDetails.setUworkat(ar.getJSONObject(i).getString("working_at"));
                                    searchUserDetails.setUworkin(ar.getJSONObject(i).getString("working_as"));
                                    searchUserDetails.setUHHC(ar.getJSONObject(i).getString("high_school"));
                                    searchUserDetails.setUSSC(ar.getJSONObject(i).getString("intermediate"));
                                    searchUserDetails.setUGradu(ar.getJSONObject(i).getString("graduation"));
                                    searchUserDetails.setUPostGra(ar.getJSONObject(i).getString("Post_graduation"));
                                    searchUserDetails.setUadd_website(ar.getJSONObject(i).getString("add_website"));
                                    searchUserDetails.setUfacebook_page(ar.getJSONObject(i).getString("facebook_page"));
                                    packageNames.add(searchUserDetails);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else
                                {
                                    Toast.makeText(FinalSearchLayout.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
