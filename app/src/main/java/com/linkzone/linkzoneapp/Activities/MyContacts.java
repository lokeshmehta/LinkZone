package com.linkzone.linkzoneapp.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.linkzone.linkzoneapp.Adapters.ContactAdapter;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.ContactHolder;
import com.linkzone.linkzoneapp.DataHolders.GetSetImg;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyContacts extends AppCompatActivity implements ApiResponse {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ContactHolder> packageNames = new ArrayList<>();
    @BindView(R.id.ArrowBack)ImageView backArrow;
    @BindView(R.id.ParentLog)LinearLayout ParenLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        ButterKnife.bind(this);
        APIWork();
        bindPackageList();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
            }

        });

    }

    private void bindPackageList()
    {
        recyclerView=(RecyclerView)findViewById(R.id.ContactRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ContactAdapter(MyContacts.this,packageNames);
        recyclerView.setAdapter(adapter);
    }

    public void APIWork()
    {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", UserDetails.getUEmail());
            Functions.showLoadingDialog(MyContacts.this, "Getting Details");
            new ApiListener(MyContacts.this, ApiUrl.GetuserContact, jsonObject).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onApiResponse(String response) {
        Functions.closeLoadingDialog();
        Log.e("Response",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray ar = jsonObject.getJSONArray("userData");

            if (jsonObject.getString("response").matches("success"))
            {
                packageNames.clear();
                for(int i=0;i<ar.length();i++)
                {
                    ContactHolder contactHolder = new ContactHolder();
                    contactHolder.setUName(ar.getJSONObject(i).getString("user_name"));
                    contactHolder.setUID(ar.getJSONObject(i).getString("id"));
                    contactHolder.setUEmail(ar.getJSONObject(i).getString("user_email"));
                    contactHolder.setUImage(ar.getJSONObject(i).getString("image"));
                    packageNames.add(contactHolder);
                }
                adapter.notifyDataSetChanged();
            }
            else if(jsonObject.getString("response").matches("no friends"))
            {
                Functions.showErrorMessage(ParenLay,"Seems that you don't have any friends",MyContacts.this);
            }
        } catch (JSONException e) {

        }

    }
}
