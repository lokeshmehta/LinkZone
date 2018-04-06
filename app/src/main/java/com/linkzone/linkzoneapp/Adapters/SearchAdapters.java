package com.linkzone.linkzoneapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.linkzone.linkzoneapp.Activities.SeeProfile;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.Home_Profiles;
import com.linkzone.linkzoneapp.DataHolders.SearchUserDetails;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tiwaris on 3/3/2017.
 */

public class SearchAdapters extends RecyclerView.Adapter<SearchAdapters.Items> {
    Context context;
    ArrayList<SearchUserDetails> packageNames;

    public SearchAdapters(Context context, ArrayList<SearchUserDetails> packageNames) {
        this.context = context;
        this.packageNames = packageNames;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.final_search_data, parent, false);
        return new Items(v);
    }

    @Override
    public void onBindViewHolder(final Items holder, final int position) {
        SearchUserDetails searchUserDetails = packageNames.get(position);

        holder.DpDesig.setText(searchUserDetails.getUworkin());
       // double val=Math.round(Double.parseDouble(searchUserDetails.getUDistenace().toString()));
       // holder.DpDistance.setText(String.valueOf(val)+"M");

        holder.DpName.setText(searchUserDetails.getUName());
        holder.DpWorkAt.setText("Working At : "+searchUserDetails.getUworkat());
        holder.DpLives.setText("Lives In : "+searchUserDetails.getUlives());

        Picasso.with(context)
                .load("http://xperiaindia.com/linkZone/userImages/"+searchUserDetails.getUID()+"/"+packageNames.get(position).getUImage())
                .placeholder(R.drawable.dpplaceh)
                .into(holder.ProfileDp);

        Log.e("SearchImage",packageNames.get(position).getUImage());
    }


    @Override
    public int getItemCount() {
        return packageNames.size();
    }

    public class Items extends RecyclerView.ViewHolder {
        MyBoldTextView DpName;
        ImageView FavIco;
        MyTextView DpDesig, DpLives, DpWorkAt, sendRqst,AddFav;
        TextView DpDistance;
        CircleImageView ProfileDp;

        public Items(View itemView) {
            super(itemView);

            ProfileDp=(CircleImageView)itemView.findViewById(R.id.ProfileDp);
            DpName = (MyBoldTextView) itemView.findViewById(R.id.DPname);
            DpLives = (MyTextView) itemView.findViewById(R.id.Lives);
            DpDesig = (MyTextView) itemView.findViewById(R.id.Designation);
            DpWorkAt = (MyTextView) itemView.findViewById(R.id.UWorkAt);
            DpDistance = (TextView) itemView.findViewById(R.id.UDistance);
            sendRqst=(MyTextView)itemView.findViewById(R.id.SendRequest);
            AddFav=(MyTextView)itemView.findViewById(R.id.AddFav);
            FavIco=(ImageView) itemView.findViewById(R.id.FavIco);


            sendRqst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("msg","Wants to be your friend");
                        jsonObject.put("fromUId",UserDetails.getUID());
                        jsonObject.put("fromName",UserDetails.getUName());
                        jsonObject.put("toUId",packageNames.get(getAdapterPosition()).getUID());
                        jsonObject.put("imageurl",packageNames.get(getAdapterPosition()).getUImage());
                        jsonObject.put("fromEmail",UserDetails.getUEmail());
                        jsonObject.put("toEmail",packageNames.get(getAdapterPosition()).getUEmail());
                        jsonObject.put("messageType","");
                        Log.e("FCMDataSend",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AndroidNetworking.initialize(context);
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
                                        sendRqst.setText("Requested");
                                        sendRqst.setClickable(false);
                                        notification(UserDetails.getUEmail(),packageNames.get(getAdapterPosition()).getUEmail(),"Link request send to "+packageNames.get(getAdapterPosition()).getUName(),"2");
                                    }
                                    Log.e("FCMDataResponse",response);
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });

                }
            });

            AddFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_id",UserDetails.getUID());
                        jsonObject.put("user_email",UserDetails.getUEmail());
                        jsonObject.put("favorite_user_id",packageNames.get(getAdapterPosition()).getUID());
                        jsonObject.put("favorite_user_email",packageNames.get(getAdapterPosition()).getUEmail());
                        Log.e("AddToFav",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AndroidNetworking.initialize(context);
                    AndroidNetworking.post(ApiUrl.FavoriteUser)
                            .setPriority(Priority.MEDIUM)
                            .setTag("Token")
                            .addJSONObjectBody(jsonObject)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        if (response.getString("response").equals("success"))

                                        {
                                            Toast.makeText(context, "Added to favorite list", Toast.LENGTH_SHORT).show();
                                            AddFav.setText("Star");
                                            FavIco.setImageResource(R.drawable.ic_addtofav);
                                            notification(UserDetails.getUEmail(),packageNames.get(getAdapterPosition()).getUEmail(),"You favorited "+packageNames.get(getAdapterPosition()).getUName(),"1");

                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            });
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

            AndroidNetworking.initialize(context);
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