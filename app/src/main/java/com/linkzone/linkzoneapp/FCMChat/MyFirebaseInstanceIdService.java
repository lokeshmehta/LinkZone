package com.linkzone.linkzoneapp.FCMChat;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token", "Refreshed token: "+recent_token);
        UserDetails.setChat_id(recent_token);
    }
}
