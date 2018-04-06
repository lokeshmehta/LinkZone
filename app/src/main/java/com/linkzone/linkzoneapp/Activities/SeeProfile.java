package com.linkzone.linkzoneapp.Activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.linkzone.linkzoneapp.Adapters.Recycler_Cell_adapter;
import com.linkzone.linkzoneapp.Adapters.SeeProImageAdapter;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.GetSetImg;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.linkzone.linkzoneapp.widget.RippleView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SeeProfile extends AppCompatActivity {

    @BindView(R.id.ExapndEducation)ExpandableLinearLayout expandEdu;
    @BindView(R.id.ExapndProfile)ExpandableLinearLayout expandPro;
    @BindView(R.id.GotoExAlb)RelativeLayout ExpAlbum;
    @BindView(R.id.GotoExEDetails)RelativeLayout ExpEdu;
    @BindView(R.id.GotoExPDeatils)RelativeLayout ExpPro;

    ////////////////////////////////////////////////////////////////////////

    @BindView(R.id.HeadName)MyBoldTextView UName;
    @BindView(R.id.HeadStatus)MyTextView UStatus;
    @BindView(R.id.ProfileDp)CircleImageView UImage;
    @BindView(R.id.UserEmail)MyTextView UEmail;
    @BindView(R.id.UserDob)MyTextView UDob;
    @BindView(R.id.UserMob)MyTextView UMob;
    @BindView(R.id.UserInte)MyTextView UInte;
    @BindView(R.id.UserLives)MyTextView ULives;
    @BindView(R.id.UserWorkAs)MyTextView UWorkAS;
    @BindView(R.id.UserWorkAt)MyTextView UWorkAt;
    @BindView(R.id.UserPG)MyTextView UPostGra;
    @BindView(R.id.UserGrad)MyTextView UGrad;
    @BindView(R.id.CoverPic)ImageView SetCover;

    ////////////////////WORK////////////////////
    @BindView(R.id.AddFav)RippleView GotoFav;
    @BindView(R.id.SendFRequest)RippleView SendRequest;
    @BindView(R.id.SendRqtText)MyBoldTextView SendRTxt;
    @BindView(R.id.Favtxt)MyBoldTextView AddFav;
    @BindView(R.id.PLikes)ImageView FavIco;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<GetSetImg> getSetImgs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);

        ButterKnife.bind(SeeProfile.this);

        if(getIntent().getStringExtra("Favorite").equalsIgnoreCase("Yes")){
            FavIco.setImageResource(R.drawable.ic_addtofav);
        }

        APIWORK();

        Picasso.with(SeeProfile.this)
                .load("http://xperiaindia.com/linkZone/userImages/"+ getIntent().getStringExtra("UID")+"/"+getIntent().getStringExtra("UImage"))
                .placeholder(R.drawable.dpplaceh)
                .into(UImage);
        Picasso.with(SeeProfile.this)
                .load("http://xperiaindia.com/linkZone/userImages/"+ getIntent().getStringExtra("UID")+"/"+getIntent().getStringExtra("CoverImg"))
                .into(SetCover);

        UImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)UImage.getDrawable()).getBitmap();
                showProfileImage(bitmap);
            }
        });

        UName.setText(getIntent().getStringExtra("Uname"));
        UStatus.setText(getIntent().getStringExtra("UStatus"));
        UEmail.setText(getIntent().getStringExtra("UEmail"));
        UDob.setText(getIntent().getStringExtra("UDob"));
        UMob.setText(getIntent().getStringExtra("UMob"));
        UInte.setText(getIntent().getStringExtra("UInterest"));
        ULives.setText(getIntent().getStringExtra("ULives"));
        UWorkAS.setText(getIntent().getStringExtra("UWorkAS"));
        UWorkAt.setText(getIntent().getStringExtra("UWorkAT"));
        UPostGra.setText(getIntent().getStringExtra("UPG"));
        UGrad.setText(getIntent().getStringExtra("UGrad"));

        ExpAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expandEdu.collapse();
                expandPro.collapse();
            }
        });
        ExpEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                expandEdu.toggle();
                expandPro.collapse();

            }
        });
        ExpPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                expandEdu.collapse();
                expandPro.toggle();

            }
        });

        GotoFav.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id",UserDetails.getUID());
                    jsonObject.put("user_email",UserDetails.getUEmail());
                    jsonObject.put("favorite_user_id",getIntent().getStringExtra("UID"));
                    jsonObject.put("favorite_user_email",getIntent().getStringExtra("UEmail"));
                    Log.e("AddToFav",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.initialize(SeeProfile.this);
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
                                        Toast.makeText(SeeProfile.this, "Added", Toast.LENGTH_SHORT).show();
                                        AddFav.setText("Added");
                                        FavIco.setImageResource(R.drawable.ic_addtofav);
                                    }
                                    else
                                    {
                                        Toast.makeText(SeeProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

        if(getIntent().getStringExtra("CheckFriend").equalsIgnoreCase("yes"))
        {
            SendRTxt.setText("Friend");
            SendRequest.setClickable(false);
        }
        else
        {
            SendRequest.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("msg","Wants to be your friend");
                        jsonObject.put("fromUId",UserDetails.getUID());
                        jsonObject.put("fromName",UserDetails.getUName());
                        jsonObject.put("toUId",getIntent().getStringExtra("UID"));
                        jsonObject.put("imageurl","");
                        jsonObject.put("fromEmail",UserDetails.getUEmail());
                        jsonObject.put("toEmail",getIntent().getStringExtra("UEmail"));
                        jsonObject.put("messageType","");
                        Log.e("FCMDataSend",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AndroidNetworking.initialize(SeeProfile.this);
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
                                        SendRTxt.setText("Requested");
                                        SendRequest.setClickable(false);
                                    }
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

    private void showProfileImage(Bitmap bitmap){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picture_alert_layout);
        dialog.setCancelable(true);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.profileIv);
        imageView.setImageBitmap(bitmap);


        dialog.show();
    }

    private void bindPackageList() {
        recyclerView = (RecyclerView) findViewById(R.id.SeeProImageRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SeeProImageAdapter(SeeProfile.this, getSetImgs, getIntent().getStringExtra("UID"));
        recyclerView.setAdapter(adapter);
    }

    public void APIWORK() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email",getIntent().getStringExtra("UEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(SeeProfile.this);
        AndroidNetworking.post(ApiUrl.GetUserGallery)
                .setPriority(Priority.MEDIUM)
                .setTag("Token")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Response", response.toString());
                        try {

                            if (response.getString("response").matches("success"))
                                try {
                                    getSetImgs.clear();
                                    JSONArray jsonarray = response.getJSONArray("userGallery");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        GetSetImg getSetImg = new GetSetImg();
                                        getSetImg.setImagePath(jsonarray.getJSONObject(i).getString("image_name"));
                                        getSetImg.setImageId(jsonarray.getJSONObject(i).getString("id"));
                                        getSetImgs.add(getSetImg);
                                    }
                                    bindPackageList();

                                } catch (Exception e) {
                                    Log.e("Response", e.getMessage());
                                }
                            else {
                                if (response.getString("response").matches("no data found")) {

                                    //Toast.makeText(SeeProfile.this, "No Image Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            Functions.closeLoadingDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }

                });
    }
}
