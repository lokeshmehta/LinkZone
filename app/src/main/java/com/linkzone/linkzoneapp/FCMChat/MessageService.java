package com.linkzone.linkzoneapp.FCMChat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;




public class MessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)

    {
//        ChatEndDataBase chatEndDataBase=new ChatEndDataBase(getApplicationContext());
//        ChatIndFunctions.printLog("Notification",remoteMessage.getData().toString());
//       if(remoteMessage.getData().get("chatType").equalsIgnoreCase("1"))
//       {
//           //Normal Chat
//           String title= remoteMessage.getData().get(ApiKeys.USER_NAME);
//           String msg= remoteMessage.getData().get(ApiKeys.MESSAGE);
//           String img= remoteMessage.getData().get(ApiKeys.IMAGE_URL);
//           String messageType=remoteMessage.getData().get(ApiKeys.MESSAGE_TYPE);
//           String toId= remoteMessage.getData().get(ApiKeys.TO_USER_ID);
//           String fromId= remoteMessage.getData().get(ApiKeys.FROM_USER_ID);
//           String msgDateTime=remoteMessage.getData().get(ApiKeys.MESSAGE_DATE_TIME);
//           ChatIndFunctions.printLog(" Name:>", title+ " Message:>" + msg + " FromId:>" + fromId + " ToId:>" + toId);
//           ChatIndFunctions.printLog(" CurrentChatID:>", UserDetails.getCurrentChatId()+ " FromId:>" + fromId + " ToId:>" + toId);
//           if (!NotificationHandler.isAppIsInBackground(getApplicationContext()))
//           {
//               if(UserDetails.getCurrentChatId().equalsIgnoreCase(fromId))
//               {
//                   chatEndDataBase.insertChat(fromId,toId,msg,img,messageType,"1",msgDateTime);
//                   Intent intent=new Intent("Chat");
//                   intent.putExtra(ApiKeys.MESSAGE,msg);
//                   intent.putExtra(ApiKeys.TO_USER_ID,toId);
//                   intent.putExtra(ApiKeys.FROM_USER_ID,fromId);
//                   intent.putExtra(ApiKeys.MESSAGE_DATE_TIME,msgDateTime);
//                   intent.putExtra(ApiKeys.MESSAGE_TYPE,messageType);
//                   intent.putExtra(ApiKeys.IMAGE_URL,img);
//                   LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//               }else
//               {
//                   chatEndDataBase.insertChat(fromId,toId,msg,img,messageType,"0",msgDateTime);
//                   showNotificationMessage(msgDateTime,fromId,toId,title,msg,img,getApplicationContext());
//
//                   Intent intent=new Intent("BadgeCount");
//                   intent.putExtra(ApiKeys.TO_USER_ID,toId);
//                   intent.putExtra(ApiKeys.FROM_USER_ID,fromId);
//                   LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//               }
//           } else
//           {
//               chatEndDataBase.insertChat(fromId,toId,msg,img,messageType,"0",msgDateTime);
//               showNotificationMessage(msgDateTime,fromId,toId,title,msg,img,getApplicationContext());
//
//               Intent intent=new Intent("BadgeCount");
//               intent.putExtra(ApiKeys.TO_USER_ID,toId);
//               intent.putExtra(ApiKeys.FROM_USER_ID,fromId);
//               LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//           }
//       }
//       else if(remoteMessage.getData().get("chatType").equalsIgnoreCase("2"))
//       {
//           //Group Chat
//           String senderName=remoteMessage.getData().get(ApiKeys.SENDER_NAME);
//           String senderId=remoteMessage.getData().get(ApiKeys.SENDER_ID);
//           String groupId=remoteMessage.getData().get(ApiKeys.GROUP_ID);
//           String groupName=remoteMessage.getData().get(ApiKeys.GROUP_NAME);
//           String groupMessage=remoteMessage.getData().get(ApiKeys.GROUP_MESSAGE);
//           String groupMsgDateTime=remoteMessage.getData().get(ApiKeys.GROUP_MSG_DATE_TIME);
//           ChatIndFunctions.printLog("Group Notification ",senderName + " " + senderId  + " " + groupName + " " + groupId + " " + groupMessage + " " + groupMsgDateTime);
//           if (!NotificationHandler.isAppIsInBackground(getApplicationContext()))
//           {
//             if(UserDetails.getCurrentChatId().equalsIgnoreCase(groupId))
//             {
//                 Intent intent=new Intent("GroupChat");
//                 intent.putExtra(ApiKeys.GROUP_MESSAGE,groupMessage);
//                 intent.putExtra(ApiKeys.GROUP_NAME, groupName);
//                 intent.putExtra(ApiKeys.GROUP_ID,groupId);
//                 intent.putExtra(ApiKeys.SENDER_ID,senderId);
//                 intent.putExtra(ApiKeys.SENDER_NAME,senderName);
//                 intent.putExtra(ApiKeys.GROUP_MSG_DATE_TIME,groupMsgDateTime);
//                 LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//             }else
//             {
//                 showNotificationMessageGroup(senderName,senderId,groupName,groupId,groupMessage,groupMsgDateTime,"img here",getApplicationContext());
//             }
//           } else
//           {
//               showNotificationMessageGroup(senderName,senderId,groupName,groupId,groupMessage,groupMsgDateTime,"img here",getApplicationContext());
//           }
//       }
//    }
//
//    public void showNotificationMessage(String msgDateTime,String fromId,String toId,finallogo String title, finallogo String message,String img,finallogo Context mContext)
//    {
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        finallogo NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            builder.setSmallIcon(R.drawable.ic_logo);
//            builder.setColor(mContext.getResources().getColor(R.color.colorPrimary));
//        }
//        else
//        {
//            builder.setSmallIcon(R.drawable.ic_logo);
//        }
//
//        Intent intent = new Intent("NotiTarget");
//        Bundle b=new Bundle();
//        b.putString(ApiKeys.MESSAGE,message);
//        b.putString(ApiKeys.FROM_USER_ID, fromId);
//        b.putString(ApiKeys.TO_USER_ID,fromId);
//        b.putString(ApiKeys.USER_NAME,title);
//        b.putString(ApiKeys.NOTIFY_TYPE,"1");
//        b.putString(ApiKeys.MESSAGE_DATE_TIME,msgDateTime);
//        intent.putExtras(b);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//
//        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_logo));
//        builder.setContentTitle(title);
//        builder.setContentText(message);
//        builder.setAutoCancel(true);
//        builder.setPriority(Notification.PRIORITY_HIGH);
//        builder.setSound(sound);
//        try
//        {
//            ImageRequest imageRequest=new ImageRequest(img, new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap response) {
//                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
//                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//                    notificationManager.notify(new Random().nextInt(), builder.build());
//                }
//            }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//                    notificationManager.notify(new Random().nextInt(), builder.build());
//                }
//            });
//            MySingleton.getMySingleton(this).addToRequestQueue(imageRequest);
//        }catch (Exception e)
//        {
//            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//            notificationManager.notify(new Random().nextInt(), builder.build());
//        }
//    }
//
//    public void showNotificationMessageGroup(String senderName,String senderId,String groupName,String groupId,String groupMessage,String groupMsgDateTime,String img,finallogo Context mContext)
//    {
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        finallogo NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            builder.setSmallIcon(R.drawable.ic_logo);
//            builder.setColor(mContext.getResources().getColor(R.color.colorPrimary));
//        }
//        else
//        {
//            builder.setSmallIcon(R.drawable.ic_logo);
//        }
//
//        Intent intent = new Intent("GroupTarget");
//        Bundle b=new Bundle();
//        b.putString(ApiKeys.GROUP_MESSAGE,groupMessage);
//        b.putString(ApiKeys.GROUP_NAME, groupName);
//        b.putString(ApiKeys.GROUP_ID,groupId);
//        b.putString(ApiKeys.SENDER_ID,senderId);
//        b.putString(ApiKeys.SENDER_NAME,senderName);
//        b.putString(ApiKeys.NOTIFY_TYPE,"1");
//        b.putString(ApiKeys.GROUP_MSG_DATE_TIME,groupMsgDateTime);
//        intent.putExtras(b);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//
//        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_logo));
//        builder.setContentTitle(groupName +" " + senderName);
//        builder.setContentText(groupMessage);
//        builder.setAutoCancel(true);
//        builder.setPriority(Notification.PRIORITY_HIGH);
//        builder.setSound(sound);
//        try
//        {
//            ImageRequest imageRequest=new ImageRequest(img, new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap response) {
//                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
//                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//                    notificationManager.notify(new Random().nextInt(), builder.build());
//                }
//            }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//                    notificationManager.notify(new Random().nextInt(), builder.build());
//                }
//            });
//            MySingleton.getMySingleton(this).addToRequestQueue(imageRequest);
//        }catch (Exception e)
//        {
//            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//            notificationManager.notify(new Random().nextInt(), builder.build());
//        }
//    }
//}
    }
}