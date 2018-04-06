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
import com.linkzone.linkzoneapp.DataHolders.Home_Profiles;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.RippleView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeeProImageAdapter extends RecyclerView.Adapter<SeeProImageAdapter.Items> {
    Context context;
    RecyclerView rec;
    String id;
    public static ArrayList<GetSetImg> GetsetimgArrayList;
    public SeeProImageAdapter(Context context, ArrayList<GetSetImg> GetsetimgArrayList, String id) {
        this.context = context;
        this.GetsetimgArrayList = GetsetimgArrayList;
        this.id = id;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_cell, parent, false);
        return new Items(v);
    }

    @Override
    public void onBindViewHolder(final Items holder, final int position) {

        Picasso.with(context)
                .load("http://xperiaindia.com/linkZone/userImages/"+id+"/"+GetsetimgArrayList.get(position).getImagePath())
                .into(holder.showimg);

        Log.e("Prinr","http://xperiaindia.com/linkZone/userImages/"+id+"/"+GetsetimgArrayList.get(position).getImagePath());
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
            pd = new ProgressDialog(context);
            pd.setMessage("Please Wait");
            showimg = (ImageView) itemView.findViewById(R.id.showimg);
            showimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, Image_slider.class);

                    String imgLink;
                    ArrayList<String> arr = new ArrayList<String>();
                    for (int i = 0; i < GetsetimgArrayList.size(); i++) {
                        arr.add("http://xperiaindia.com/linkZone/userImages/" + id +"/"+ GetsetimgArrayList.get(i).getImagePath());
                    }

                    intent.putExtra("image", arr);
                    intent.putExtra("adapterPosition", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
            showimg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });


        }
    }
}