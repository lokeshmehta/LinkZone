package com.linkzone.linkzoneapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.FCMChat.ApiKeys;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.linkzone.linkzoneapp.widget.RippleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Notification extends AppCompatActivity implements ApiResponse {

    @BindView(R.id.ProfileDp)CircleImageView UImage;
    @BindView(R.id.Uname)MyBoldTextView UName;
    @BindView(R.id.UMsg)MyTextView UMsj;
    @BindView(R.id.UAccpt)RippleView NotiAccept;
    @BindView(R.id.UDecline)RippleView NotiDecline;

    String touid,frmid,toemail,fromemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        UName.setText(getIntent().getExtras().getString(ApiKeys.USER_NAME));
        UMsj.setText(getIntent().getExtras().getString(ApiKeys.msg));


        frmid=getIntent().getExtras().getString(ApiKeys.fromUId);
        touid=getIntent().getExtras().getString(ApiKeys.toUId);
        toemail=getIntent().getExtras().getString(ApiKeys.toEmail);
        fromemail=getIntent().getExtras().getString(ApiKeys.fromEmail);
        NotiAccept.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                APIWork();
            }
        });

        NotiDecline.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                Notification.this.finish();
            }
        });

    }
    public void APIWork()
    {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",frmid);
            jsonObject.put("user_email", fromemail);
            jsonObject.put("friend_id",touid);
            jsonObject.put("friend_email",toemail);
            Log.e("FriendSend",jsonObject.toString());
            Functions.showLoadingDialog(Notification.this, "Saving Data Please Wait");
            new ApiListener(Notification.this, ApiUrl.AcceptFriendRequest, jsonObject).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApiResponse(String response) {

        Log.e("FriendResponse",response);
        Functions.closeLoadingDialog();
        try {
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.getString("response").matches("success"));
            {
                Toast.makeText(this, "Accepted", Toast.LENGTH_SHORT).show();
                notification(getIntent().getExtras().getString(ApiKeys.fromUId),UserDetails.getUEmail(),"Link Request Accepted","4");

                // AcceptResponse();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AcceptResponse()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg","Friend Request Accepted");
            jsonObject.put("fromUId",UserDetails.getUID());
            jsonObject.put("fromName",UserDetails.getUName());
            jsonObject.put("toUId",getIntent().getExtras().getString(ApiKeys.fromUId));
            jsonObject.put("imageurl","");
            jsonObject.put("fromEmail",UserDetails.getUEmail());
            jsonObject.put("toEmail",getIntent().getExtras().getString(ApiKeys.fromEmail));
            jsonObject.put("messageType","");
            Log.e("FCMDataSend",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(Notification.this);
        AndroidNetworking.post(ApiUrl.SendNoti)
                .setPriority(Priority.MEDIUM)
                .setTag("Token")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.matches("Submitted....."))
                        {
                            Intent intent = new Intent(Notification.this, SplashScreen.class);
                            startActivity(intent);
                            finish();
                        }
                        Log.e("FCMDataResponse",response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Notification.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
        finish();
    }

    public void notification(String fromEmail,String toEmail, String message, String flag){

        JSONObject jsonObject = new JSONObject();
        try {

            Calendar c = Calendar.getInstance();

            String dayStr;
            if(c.get(Calendar.DAY_OF_MONTH)<10){
                dayStr = "0"+c.get(Calendar.DAY_OF_MONTH);
            }else dayStr = ""+c.get(Calendar.DAY_OF_MONTH);

            String monthStr;
            if(c.get(Calendar.MONTH)+1<10){
                monthStr = "0"+(c.get(Calendar.MONTH)+1);
            }else monthStr = ""+(c.get(Calendar.MONTH)+1);

            String hourStr;
            if(c.get(Calendar.HOUR_OF_DAY)<10){
                hourStr = "0"+c.get(Calendar.HOUR_OF_DAY);
            }else hourStr = ""+c.get(Calendar.HOUR_OF_DAY);

            String minuteStr;
            if(c.get(Calendar.MINUTE)<10){
                minuteStr = "0"+c.get(Calendar.MINUTE);
            }else minuteStr = ""+c.get(Calendar.MINUTE);

            String secondStr;
            if(c.get(Calendar.SECOND)<10){
                secondStr = "0"+c.get(Calendar.SECOND);
            }else secondStr = ""+c.get(Calendar.SECOND);


            String sDate = c.get(Calendar.YEAR) + "-"
                    + monthStr
                    + "-" + dayStr
                    + " " + hourStr
                    + ":" + minuteStr
                    + ":" + secondStr;

            jsonObject.put("send_user_email",fromEmail);
            jsonObject.put("recive_user_email",toEmail);
            jsonObject.put("messgae_content",message);
            jsonObject.put("request_time",sDate);
            jsonObject.put("flag",flag);

            Log.e("NotificationParams", jsonObject.toString());

            AndroidNetworking.initialize(Notification.this);
            AndroidNetworking.post(ApiUrl.SendNotification)
                    .setPriority(Priority.MEDIUM)
                    .setTag("Token")
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("NotificationResponse",response);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
