package com.linkzone.linkzoneapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class HomeAdapters extends RecyclerView.Adapter<HomeAdapters.Items> {
    Context context;
    ArrayList<Home_Profiles> packageNames;

    //int lastPosition = -1;

    public HomeAdapters(Context context, ArrayList<Home_Profiles> packageNames) {
        this.context = context;
        this.packageNames = packageNames;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_data, parent, false);
        return new Items(v);
    }

    @Override
    public void onBindViewHolder(final Items holder, final int position) {
        Home_Profiles home_profiles = packageNames.get(position);
        holder.DpDesig.setText(home_profiles.getUWorkAs());
        double val=Math.round(Double.parseDouble(home_profiles.getUDistenace().toString())*1.6);
        holder.DpDistance.setText(String.valueOf(val)+"KM");

        holder.DpName.setText(home_profiles.getUName());
        holder.DpWorkAt.setText("Working At : "+home_profiles.getUWorkAt());
        holder.DpLives.setText("Lives In : "+home_profiles.getULive());

        if(home_profiles.getIs_favorite().matches("yes"))
        {
            holder.FavIco.setImageResource(R.drawable.ic_addtofav);
        }

        Picasso.with(context)
                .load("http://xperiaindia.com/linkZone/userImages/"+packageNames.get(position).getUID()+"/"+packageNames.get(position).getUImage())
                .resize(100,100)
                .placeholder(R.drawable.dpplaceh)
                .into(holder.ProfileDp);

//        Log.e("ValueHomeImage","http://xperiaindia.com/linkZone/userImages/"+home_profiles.getUID()+"/"+packageNames.get(position).getUImage());

        if(packageNames.get(position).getFriend().matches("yes"))
        {
            holder.sendRqst.setText("Friend");
        }

//        if(position >lastPosition) {
//
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycl);
//            holder.itemView.startAnimation(animation);
//            lastPosition = position;
//        }
    }


    @Override
    public int getItemCount() {
        return packageNames.size();
    }

    public class Items extends RecyclerView.ViewHolder {
        MyBoldTextView DpName;
        MyTextView DpDesig,DpLives,DpWorkAt,sendRqst,AddFav;
        TextView DpDistance;
        ImageView PopMenu,FavIco;
        ImageView ProDp;
        CircleImageView ProfileDp;
        LinearLayout OpenPro;

        public Items(View itemView) {
            super(itemView);
            ProfileDp=(CircleImageView)itemView.findViewById(R.id.ProfileDp);
            DpName = (MyBoldTextView) itemView.findViewById(R.id.DPname);
            DpLives = (MyTextView) itemView.findViewById(R.id.Lives);
            DpDesig = (MyTextView) itemView.findViewById(R.id.Designation);
            DpWorkAt = (MyTextView) itemView.findViewById(R.id.UWorkAt);
            DpDistance = (TextView) itemView.findViewById(R.id.UDistance);
            sendRqst=(MyTextView)itemView.findViewById(R.id.SendRequest);
            FavIco=(ImageView)itemView.findViewById(R.id.FavIco);
            AddFav=(MyTextView)itemView.findViewById(R.id.AddtoFav);
            PopMenu = (ImageView) itemView.findViewById(R.id.DPMenu);
            ProDp=(ImageView)itemView.findViewById(R.id.ProfileDp);
            OpenPro=(LinearLayout)itemView.findViewById(R.id.Lay1);
            PopMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    packageNames.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), packageNames.size());
                }
            });
            ProDp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeeProfile.class);
                    intent.putExtra("Uname",packageNames.get(getAdapterPosition()).getUName());
                    intent.putExtra("UEmail",packageNames.get(getAdapterPosition()).getUEmail());
                    intent.putExtra("UMob",packageNames.get(getAdapterPosition()).getUMobile());
                    intent.putExtra("UID",packageNames.get(getAdapterPosition()).getUID());
                    intent.putExtra("UImage",packageNames.get(getAdapterPosition()).getUImage());
                    intent.putExtra("UDesignation",packageNames.get(getAdapterPosition()).getUDesig());
                    intent.putExtra("UDob",packageNames.get(getAdapterPosition()).getUDob());
                    intent.putExtra("UStatus",packageNames.get(getAdapterPosition()).getUStatus());
                    intent.putExtra("UInterest",packageNames.get(getAdapterPosition()).getUInte());
                    intent.putExtra("ULives",packageNames.get(getAdapterPosition()).getULive());
                    intent.putExtra("UWorkAS",packageNames.get(getAdapterPosition()).getUWorkAs());
                    intent.putExtra("UWorkAT",packageNames.get(getAdapterPosition()).getUWorkAt());
                    intent.putExtra("UHHC",packageNames.get(getAdapterPosition()).getUHHC());
                    intent.putExtra("USSC",packageNames.get(getAdapterPosition()).getUSSC());
                    intent.putExtra("UGrad",packageNames.get(getAdapterPosition()).getUGrad());
                    intent.putExtra("UPG",packageNames.get(getAdapterPosition()).getUPGrad());
                    intent.putExtra("UFBPage",packageNames.get(getAdapterPosition()).getUFacebook());
                    intent.putExtra("UWebsite",packageNames.get(getAdapterPosition()).getUWeb());
                    intent.putExtra("CheckFriend",packageNames.get(getAdapterPosition()).getFriend());
                    intent.putExtra("CoverImg",packageNames.get(getAdapterPosition()).getCoverImg());
                    intent.putExtra("Favorite",packageNames.get(getAdapterPosition()).getIs_favorite());
                    context.startActivity(intent);
                }
            });

            OpenPro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeeProfile.class);
                    intent.putExtra("Uname",packageNames.get(getAdapterPosition()).getUName());
                    intent.putExtra("UEmail",packageNames.get(getAdapterPosition()).getUEmail());
                    intent.putExtra("UMob",packageNames.get(getAdapterPosition()).getUMobile());
                    intent.putExtra("UID",packageNames.get(getAdapterPosition()).getUID());
                    intent.putExtra("UImage",packageNames.get(getAdapterPosition()).getUImage());
                    intent.putExtra("UDesignation",packageNames.get(getAdapterPosition()).getUDesig());
                    intent.putExtra("UDob",packageNames.get(getAdapterPosition()).getUDob());
                    intent.putExtra("UStatus",packageNames.get(getAdapterPosition()).getUStatus());
                    intent.putExtra("UInterest",packageNames.get(getAdapterPosition()).getUInte());
                    intent.putExtra("ULives",packageNames.get(getAdapterPosition()).getULive());
                    intent.putExtra("UWorkAS",packageNames.get(getAdapterPosition()).getUWorkAs());
                    intent.putExtra("UWorkAT",packageNames.get(getAdapterPosition()).getUWorkAt());
                    intent.putExtra("UHHC",packageNames.get(getAdapterPosition()).getUHHC());
                    intent.putExtra("USSC",packageNames.get(getAdapterPosition()).getUSSC());
                    intent.putExtra("UGrad",packageNames.get(getAdapterPosition()).getUGrad());
                    intent.putExtra("UPG",packageNames.get(getAdapterPosition()).getUPGrad());
                    intent.putExtra("UFBPage",packageNames.get(getAdapterPosition()).getUFacebook());
                    intent.putExtra("UWebsite",packageNames.get(getAdapterPosition()).getUWeb());
                    intent.putExtra("CheckFriend",packageNames.get(getAdapterPosition()).getFriend());
                    intent.putExtra("CoverImg",packageNames.get(getAdapterPosition()).getCoverImg());
                    intent.putExtra("Favorite",packageNames.get(getAdapterPosition()).getIs_favorite());
                    context.startActivity(intent);
                }
            });

                sendRqst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!sendRqst.getText().equals("Friend")){
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

                    }
                });

            FavIco.setOnClickListener(new View.OnClickListener() {
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

                                            Home_Profiles home_profiles = packageNames.get(getAdapterPosition());
                                            home_profiles.setIs_favorite("Yes");

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