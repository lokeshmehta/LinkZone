package com.linkzone.linkzoneapp.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.linkzone.linkzoneapp.Activities.Home;
import com.linkzone.linkzoneapp.Activities.Image_slider;
import com.linkzone.linkzoneapp.Activities.ShowImageGall;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.GetSetImg;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.RippleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recycler_Cell_adapter extends RecyclerView.Adapter<Recycler_Cell_adapter.Items> {
    Context context;
    RecyclerView rec;
    public ArrayList<GetSetImg> GetsetimgArrayList;
    public Recycler_Cell_adapter(Context context, ArrayList<GetSetImg> GetsetimgArrayList) {
        this.context = context;
        this.GetsetimgArrayList = GetsetimgArrayList;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_cell, parent, false);
        return new Items(v);
    }

    @Override
    public void onBindViewHolder(final Items holder, final int position) {

        Glide.with(context)
                .load("http://xperiaindia.com/linkZone/userImages/"+UserDetails.getUID()+"/"+GetsetimgArrayList.get(position).getImagePath())
                .into(holder.showimg);
    }
    @Override
    public int getItemCount() {
        return GetsetimgArrayList.size();
    }

    public class Items extends RecyclerView.ViewHolder {

        ImageView showimg;
        ProgressDialog pd;
        public Items(View itemView) {
            super(itemView);
            pd =new ProgressDialog(context);
            pd.setMessage("Please Wait");
            showimg=(ImageView)itemView.findViewById(R.id.showimg);
            showimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, Image_slider.class);

                    String imgLink;
                    ArrayList<String> arr =new ArrayList<String>();
                    for (int i=0;i<GetsetimgArrayList.size();i++){
                        arr.add("http://xperiaindia.com/linkZone/userImages/"+UserDetails.getUID()+"/"+GetsetimgArrayList.get(i).getImagePath());
                    }

                    intent.putExtra("image",arr);
                    intent.putExtra("adapterPosition",getAdapterPosition());
                    context.startActivity(intent);
                }
            });
            showimg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showLogoutDialog();
                    return true;
                }
            });


        }

        AlertDialog logout;

        public void showLogoutDialog() {
            AlertDialog.Builder adb = new AlertDialog.Builder(context, R.style.ForgotP);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_image, null);
            adb.setView(view);
            logout = adb.create();
            logout.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
            logout.setCancelable(true);

            RippleView DeleteImg, MakeImg,MakeCoverPic;
            DeleteImg = (RippleView) view.findViewById(R.id.DeletePic);
            MakeCoverPic = (RippleView) view.findViewById(R.id.MakeCoverPic);
            DeleteImg.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {

                }
            });

            MakeImg = (RippleView) view.findViewById(R.id.MakeProfilePic);
            MakeImg.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    pd.setTitle("Applying Changes.......");
                    pd.show();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_email",UserDetails.getUEmail());
                        jsonObject.put("image_id",GetsetimgArrayList.get(getPosition()).getImagePath());
                        Log.e("ImageRes",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AndroidNetworking.initialize(context);
                    AndroidNetworking.post(ApiUrl.MakeProfilePic)
                            .setPriority(Priority.MEDIUM)
                            .setTag("Token")
                            .addJSONObjectBody(jsonObject)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.e("response",response.toString());
                                    pd.dismiss();
                                    logout.dismiss();
                                    try {
                                        if(response.getString("response").matches("success"))
                                        {
                                            Toast.makeText(context, "Changes Applied", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, Home.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            ((ShowImageGall)context).finish();

                                        }
                                        else
                                        {
                                            Toast.makeText(context, "OOps Something Went Wrong", Toast.LENGTH_SHORT).show();
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

            MakeCoverPic.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    pd.setTitle("Applying Changes.......");
                    pd.show();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_email",UserDetails.getUEmail());
                        jsonObject.put("cover_pic",GetsetimgArrayList.get(getPosition()).getImagePath());
                        Log.e("ImageRes",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AndroidNetworking.initialize(context);
                    AndroidNetworking.post(ApiUrl.SetCoverImage)
                            .setPriority(Priority.MEDIUM)
                            .setTag("Token")
                            .addJSONObjectBody(jsonObject)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.e("response",response.toString());
                                    pd.dismiss();
                                    logout.dismiss();
                                    try {
                                        if(response.getString("response").matches("success"))
                                        {
                                            Toast.makeText(context, "Changes Applied", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, Home.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            ((ShowImageGall)context).finish();

                                        }
                                        else
                                        {
                                            Toast.makeText(context, "OOps Something Went Wrong", Toast.LENGTH_SHORT).show();
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

            logout.show();
        }
    }
}