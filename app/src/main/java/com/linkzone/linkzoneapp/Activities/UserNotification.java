package com.linkzone.linkzoneapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.NotificationData;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserNotification extends AppCompatActivity {

    ArrayList<NotificationData> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);

        findViewById(R.id.ArrowBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("send_user_email", UserDetails.getUEmail());
            Log.e("Params", jsonObject.toString());

            Functions.showLoadingDialog(UserNotification.this, "Please Wait");
            AndroidNetworking.initialize(this);
            AndroidNetworking.post(ApiUrl.SenderNotification)
                    .setPriority(Priority.MEDIUM)
                    .setTag("Token")
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            Functions.closeLoadingDialog();

                            Log.e("NotificationResponse", response);
                            try {
                                JSONObject jsonObject1 = new JSONObject(response);
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                if (jsonObject1.getString("response").equals("success")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        NotificationData data = new NotificationData();
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        data.setUser_name(object.getString("user_name"));
                                        data.setImage(object.getString("image"));
                                        data.setId(object.getString("id"));
                                        data.setMessgae_content(object.getString("messgae_content"));
                                        data.setRequest_time(object.getString("request_time"));
                                        data.setY(object.getString("y"));
                                        data.setM(object.getString("m"));
                                        data.setD(object.getString("d"));
                                        data.setH(object.getString("h"));
                                        data.setI(object.getString("i"));
                                        data.setS(object.getString("s"));
                                        data.setSend_user_email(object.getString("send_user_email"));
                                        data.setRecive_user_email(object.getString("recive_user_email"));
                                        data.setFlag(object.getString("flag"));
                                        data.setReciverName(object.getString("reciverName"));
                                        data.setReciverImage(object.getString("reciverImage"));
                                        data.setReciverId(object.getString("reciverId"));
                                        notificationList.add(data);

                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_cell, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            NotificationData data = notificationList.get(position);

            if (!data.getY().equals("0")) {
                holder.UMsg.setText(data.getY() + " years ago");
            } else if (!data.getD().equals("0")) {
                holder.UMsg.setText(data.getD() + " days ago");

            } else if (!data.getH().equals("0")) {
                holder.UMsg.setText(data.getH() + " hours ago");

            } else if (!data.getI().equals("0")) {
                holder.UMsg.setText(data.getI() + " minutes ago");

            } else if (!data.getS().equals("0")) {
                holder.UMsg.setText(data.getS() + " seconds ago");
            }


            if (data.getSend_user_email().equals(UserDetails.getUEmail())) {
                Picasso.with(UserNotification.this).load("http://xperiaindia.com/linkZone/userImages/" + data.getReciverId() + "/" + data.getReciverImage())
                        .into(holder.ProfileDp);
            }else {
                Picasso.with(UserNotification.this).load("http://xperiaindia.com/linkZone/userImages/" + data.getId() + "/" + data.getImage())
                        .into(holder.ProfileDp);
            }



            Log.d("imageLink-->", "http://xperiaindia.com/linkZone/userImages/" + data.getId() + data.getImage());

//            if(data.getFlag().equals("1")&&data.getSend_user_email().equals(UserDetails.getUEmail())){
//                holder.Uname.setText("You favorited "+data.getRecive_user_email());
//            }else  holder.Uname.setText("You are favorited by "+data.getSend_user_email());
            if (data.getFlag().equals("1")) {

                if (data.getSend_user_email().equals(UserDetails.getUEmail())) {
                    holder.Uname.setText("You favorited " + data.getReciverName());
                } else {
                    holder.Uname.setText("You are favorited by " + data.getUser_name());
                }

            } else if (data.getFlag().equals("2")) {

                if (data.getSend_user_email().equals(UserDetails.getUEmail())) {
                    holder.Uname.setText("Link request sent to " + data.getReciverName());
                } else {
                    holder.Uname.setText("You received link request from " + data.getUser_name());
                }

            } else if (data.getFlag().equals("3")) {

                if (data.getSend_user_email().equals(UserDetails.getUEmail())) {
                    holder.Uname.setText("You favorited " + data.getReciverName());
                } else {
                    holder.Uname.setText("You are favorited by " + data.getUser_name());
                }

            } else if (data.getFlag().equals("4")) {

                if (data.getSend_user_email().equals(UserDetails.getUEmail())) {
                    holder.Uname.setText(data.getReciverName() + " accepted your request");
                } else {
                    holder.Uname.setText("You are linked with " + data.getUser_name());
                }

            }

        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            private CircleImageView ProfileDp;
            private MyBoldTextView Uname;
            private MyTextView UMsg;

            public MyHolder(View itemView) {
                super(itemView);
                ProfileDp = (CircleImageView) itemView.findViewById(R.id.ProfileDp);
                Uname = (MyBoldTextView) itemView.findViewById(R.id.Uname);
                UMsg = (MyTextView) itemView.findViewById(R.id.UMsg);

            }
        }
    }
}
