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
import com.linkzone.linkzoneapp.DataHolders.Fav_Data;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavAdapters extends RecyclerView.Adapter<FavAdapters.Items> {
    Context context;
    ArrayList<Fav_Data> packageNames;


    public FavAdapters(Context context, ArrayList<Fav_Data> packageNames) {
        this.context = context;
        this.packageNames = packageNames;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fav_data, parent, false);
        return new Items(v);
    }

    @Override
    public void onBindViewHolder(final Items holder, final int position) {
        Fav_Data fav_data = packageNames.get(position);
        holder.DpDesig.setText(fav_data.getUWorkAs());
//        double val=Math.round(Double.parseDouble(fav_data.getUDistenace().toString()));
//        holder.DpDistance.setText(String.valueOf(val)+"M");

        holder.DpName.setText(fav_data.getUName());
        Log.e("Name",fav_data.getUName());
        holder.DpWorkAt.setText("Working At : "+fav_data.getUWorkAt());
        holder.DpLives.setText("Lives In : "+fav_data.getULive());

        Picasso.with(context)
                .load("http://xperiaindia.com/linkZone/userImages/"+fav_data.getUID()+"/"+packageNames.get(position).getUImage())
                .placeholder(R.drawable.dpplaceh)
                .into(holder.ProfileDp);

        if(packageNames.get(position).getIs_friend().matches("yes"))
        {
            holder.sendRqst.setText("Friend");
        }
    }


    @Override
    public int getItemCount() {
        return packageNames.size();
    }

    public class Items extends RecyclerView.ViewHolder {
        MyBoldTextView DpName;
        MyTextView DpDesig,DpLives,DpWorkAt,sendRqst;
        TextView DpDistance;
        ImageView PopMenu,FavIco;
        ImageView ProDp;
        CircleImageView ProfileDp;
        LinearLayout topLin;

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
            PopMenu = (ImageView) itemView.findViewById(R.id.DPMenu);
            ProDp=(ImageView)itemView.findViewById(R.id.ProfileDp);
            topLin = (LinearLayout) itemView.findViewById(R.id.topLin);
            PopMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_email",UserDetails.getUEmail());
                        jsonObject.put("favorite_user_email",packageNames.get(getAdapterPosition()).getUEmail());
                        Log.e("RemoveData",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AndroidNetworking.initialize(context);
                    AndroidNetworking.post(ApiUrl.RemoveFavorite)
                            .setPriority(Priority.MEDIUM)
                            .setTag("Token")
                            .addJSONObjectBody(jsonObject)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                    packageNames.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), packageNames.size());
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });

                }
            });
            topLin.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("CheckFriend",packageNames.get(getAdapterPosition()).getIs_friend());
                    intent.putExtra("CoverImg",packageNames.get(getAdapterPosition()).getCover_pic());
                    intent.putExtra("Favorite","Yes");
                    context.startActivity(intent);
                }
            });

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
                                    sendRqst.setText("Requested");
                                    Log.e("FCMDataResponse",response);
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });

                }
            });
        }

    }
}