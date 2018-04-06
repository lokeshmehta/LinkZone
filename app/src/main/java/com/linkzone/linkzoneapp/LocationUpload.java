package com.linkzone.linkzoneapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.linkzone.linkzoneapp.Activities.ChatsHome;
import com.linkzone.linkzoneapp.Activities.Home;
import com.linkzone.linkzoneapp.Activities.Login;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LocationUpload extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction()!=null){

            Log.d("Latitude",intent.getStringExtra("Latitude"));
            Log.d("Longitude", intent.getStringExtra("Longitude"));

            if (Functions.getUserDetails((Activity) context).get(0) != "null") {
                try {
                    JSONObject jsonObject = new JSONObject(Functions.getUserDetails((Activity) context).get(2));
                    JSONArray jsonArray = jsonObject.getJSONArray("userData");
                    UserDetails.setUEmail(jsonArray.getJSONObject(0).getString("user_email"));


                    JSONObject jObj = new JSONObject();
                    try {

                        jObj.put("lat",intent.getStringExtra("Latitude"));
                        jObj.put("long",intent.getStringExtra("Longitude"));
                        jObj.put("email",jsonArray.getJSONObject(0).getString("user_email"));


                        AndroidNetworking.initialize(context);
                        AndroidNetworking.post("http://xperiaindia.com/linkZone/LZAPI/UpdateLatLong")
                                .setPriority(Priority.MEDIUM)
                                .setTag("Token")
                                .addJSONObjectBody(jObj)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.e("res", response.toString());

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }





        }

    }
}
