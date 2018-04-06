package com.linkzone.linkzoneapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.linkzone.linkzoneapp.Adapters.FavAdapters;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.Fav_Data;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavLayout extends AppCompatActivity implements ApiResponse {

    @BindView(R.id.ArrowBack)ImageView ArrowBack;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Fav_Data> packageNames=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_layout);
        ButterKnife.bind(this);

        APIWork();
        bindPackageList();
        ArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
            }
        });

    }

    public void APIWork()
    {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", UserDetails.getUEmail());
            Functions.showLoadingDialog(FavLayout.this, "Getting Details");
            new ApiListener(FavLayout.this, ApiUrl.GetFavoriteUser, jsonObject).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void bindPackageList()
    {
        recyclerView=(RecyclerView)findViewById(R.id.FavRecyler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new FavAdapters(FavLayout.this,packageNames);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onApiResponse(String response) {

        Functions.closeLoadingDialog();
        Log.e("FavRes",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray ar = jsonObject.getJSONArray("Data");
            if (jsonObject.getString("response").matches("success"))
            {
                packageNames.clear();
                for(int i=0;i<ar.length();i++)
                {
                    Fav_Data fav_data = new Fav_Data();
                    fav_data.setUName(ar.getJSONObject(i).getString("user_name"));
                    fav_data.setUID(ar.getJSONObject(i).getString("id"));
                    fav_data.setUEmail(ar.getJSONObject(i).getString("user_email"));
                    fav_data.setUImage(ar.getJSONObject(i).getString("image"));
                    fav_data.setUMobile(ar.getJSONObject(i).getString("user_mobile"));
                    fav_data.setUDob(ar.getJSONObject(i).getString("user_dob"));
                    fav_data.setUStatus(ar.getJSONObject(i).getString("timeline_status"));
                    fav_data.setUInte(ar.getJSONObject(i).getString("interested_in"));
                    fav_data.setULive(ar.getJSONObject(i).getString("lives_in"));
                    fav_data.setUWorkAt(ar.getJSONObject(i).getString("working_at"));
                    fav_data.setUWorkAs(ar.getJSONObject(i).getString("working_as"));
                    fav_data.setUHHC(ar.getJSONObject(i).getString("high_school"));
                    fav_data.setUSSC(ar.getJSONObject(i).getString("intermediate"));
                    fav_data.setUGrad(ar.getJSONObject(i).getString("graduation"));
                    fav_data.setUPGrad(ar.getJSONObject(i).getString("Post_graduation"));
                    fav_data.setUWeb(ar.getJSONObject(i).getString("add_website"));
                    fav_data.setUFacebook(ar.getJSONObject(i).getString("facebook_page"));
                    fav_data.setCover_pic(ar.getJSONObject(i).getString("cover_pic"));
                    fav_data.setIs_friend(ar.getJSONObject(i).getString("is_friend"));
                  //  fav_data.setFriend(ar.getJSONObject(i).getString("is_friend"));
//                    fav_data.setUDistenace(ar.getJSONObject(i).getString("distance"));
                    packageNames.add(fav_data);
                }
                adapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(FavLayout.this, "No Data Found", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {

        }
    }
}
