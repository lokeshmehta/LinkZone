package com.linkzone.linkzoneapp.FCMChat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.linkzone.linkzoneapp.Activities.ChatsHome;
import com.linkzone.linkzoneapp.Activities.Notification;
import com.linkzone.linkzoneapp.Adapters.ChatEndDataBase;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("FCMDATA", remoteMessage.getData().toString());
        String title = remoteMessage.getData().get(ApiKeys.USER_NAME);
        String msg = remoteMessage.getData().get(ApiKeys.msg);
        String img = remoteMessage.getData().get(ApiKeys.imageurl);
        String messageType = remoteMessage.getData().get(ApiKeys.messageType);
        String toId = remoteMessage.getData().get(ApiKeys.toUId);
        String fromId = remoteMessage.getData().get(ApiKeys.fromUId);
        String toEmail = remoteMessage.getData().get(ApiKeys.toEmail);
        String fromEmail = remoteMessage.getData().get(ApiKeys.fromEmail);
        // String msgDateTime=remoteMessage.getData().get(ApiKeys.MESSAGE_DATE_TIME);

        if (remoteMessage.getData().get("ChatType").equalsIgnoreCase("1")) {

            ChatEndDataBase chatEndDataBase = new ChatEndDataBase(getBaseContext());
            chatEndDataBase.insertChat(fromId, toId, msg, img, messageType, "0", "");

            Intent intent = new Intent(this, ChatsHome.class);
            Bundle b = new Bundle();
            b.putString(ApiKeys.msg, msg);
            b.putString(ApiKeys.toUId, toId);
            b.putString(ApiKeys.fromUId, fromId);
            b.putString(ApiKeys.USER_NAME, title);
            b.putString(ApiKeys.imageurl, img);
            b.putString(ApiKeys.messageType, messageType);
            b.putString("FUID",fromId);
            // b.putString(ApiKeys.MESSAGE_DATE_TIME,msgDateTime);
            intent.putExtras(b);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.iconmain);
                builder.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
            } else {
                builder.setSmallIcon(R.drawable.iconmain);
            }

            builder.setContentTitle(title);
            builder.setContentText(msg);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.iconmain);
            builder.setContentIntent(pendingIntent);
            builder.setPriority(android.app.Notification.PRIORITY_HIGH);


            //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (!NotificationHandler.isAppIsInBackground(getApplicationContext()) && IncomeChatHandel.getChatCount() == 1) {

                Intent intent1 = new Intent("Chat");
                intent1.putExtra(ApiKeys.msg, msg);
                intent1.putExtra(ApiKeys.toUId, toId);
                intent1.putExtra(ApiKeys.fromUId, fromId);
                //  intent1.putExtra(ApiKeys.MESSAGE_DATE_TIME,msgDateTime);
                intent1.putExtra(ApiKeys.messageType, messageType);
                intent1.putExtra(ApiKeys.imageurl, img);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
            } else {
                Log.e("Path","http://xperiaindia.com/linkZone/ChatImages/"+img);
                try {
                    Bitmap theBitmap = Glide.
                            with(this).
                            load("http://xperiaindia.com/linkZone/ChatImages/"+img).
                            asBitmap().
                            into(100, 100). // Width and height
                            get();
                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(theBitmap));
                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                                notificationManager.notify(new Random().nextInt(), builder.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(new Random().nextInt(), builder.build());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(new Random().nextInt(), builder.build());
                }
//                Glide.with(getApplicationContext())
//                        .load("http://xperiaindia.com/linkZone/ChatImages/"+img)
//                        .asBitmap()
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));
//                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//                                notificationManager.notify(new Random().nextInt(), builder.build());
//                            }
//                        });
            }

        } else

        {

            ////////////////////*************NOTIFICATION FILE***************/////////////////////////

           // notification(toEmail,fromEmail,"You received link request from "+title,"3");

            Intent intent = new Intent(this, Notification.class);
            Bundle b = new Bundle();
            b.putString(ApiKeys.msg, msg);
            b.putString(ApiKeys.toUId, toId);
            b.putString(ApiKeys.fromUId, fromId);
            b.putString(ApiKeys.USER_NAME, title);
            b.putString(ApiKeys.imageurl, img);
            b.putString(ApiKeys.toEmail, toEmail);
            b.putString(ApiKeys.fromEmail, fromEmail);
            //b.putString(ApiKeys.NOTIFY_TYPE,"1");
            // b.putString(ApiKeys.MESSAGE_DATE_TIME,msgDateTime);
            intent.putExtras(b);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.iconmain);
                builder.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
            } else {
                builder.setSmallIcon(R.drawable.iconmain);
            }

            builder.setContentTitle(title);
            builder.setContentText(msg);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.iconmain);
            builder.setContentIntent(pendingIntent);
            builder.setSound(sound);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            notificationManager.notify(new Random().nextInt(), builder.build());

        }

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

            AndroidNetworking.initialize(getBaseContext());
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
