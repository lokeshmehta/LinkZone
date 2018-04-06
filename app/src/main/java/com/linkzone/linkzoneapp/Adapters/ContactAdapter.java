package com.linkzone.linkzoneapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkzone.linkzoneapp.Activities.ChatsHome;
import com.linkzone.linkzoneapp.DataHolders.ContactHolder;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.FCMChat.ApiKeys;
import com.linkzone.linkzoneapp.FCMChat.IncomeChatHandel;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tiwaris on 3/3/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Items> {
    Context context;
    ArrayList<ContactHolder> packageNames;

    public ContactAdapter(Context context, ArrayList<ContactHolder> packageNames) {
        this.context = context;
        this.packageNames = packageNames;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contactlist_activity, parent, false);
        return new Items(v);
    }

    @Override
    public void onBindViewHolder(final Items holder, final int position) {
        ContactHolder contactHolder = packageNames.get(position);

        holder.CName.setText(contactHolder.getUName());
        ChatEndDataBase chatEndDataBase = new ChatEndDataBase(context);
        int count = chatEndDataBase.getBadgesCount(contactHolder.getUID(), UserDetails.getUID());
        holder.countTv.setText(count + "");
        Cursor lstMSg = chatEndDataBase.getlastMessage(contactHolder.getUID(), UserDetails.getUID());

        if (lstMSg != null & lstMSg.getCount() > 0) {
            lstMSg.moveToFirst();
            holder.msgTv.setText(lstMSg.getString(1)+"");
        }

        if (count == 0) {
            holder.countTv.setVisibility(View.GONE);
            holder.msgBadge.setVisibility(View.GONE);
        } else {
            holder.countTv.setVisibility(View.VISIBLE);
            holder.msgBadge.setVisibility(View.VISIBLE);
        }


        Picasso.with(context)
                .load("http://xperiaindia.com/linkZone/userImages/" + contactHolder.getUID() + "/" + packageNames.get(position).getUImage())
                .placeholder(R.drawable.dpplaceh)
                .into(holder.ProfileDp);

        holder.ProfileDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) holder.ProfileDp.getDrawable()).getBitmap();
                showProfileImage(bitmap);
            }
        });
    }


    @Override
    public int getItemCount() {
        return packageNames.size();
    }

    public class Items extends RecyclerView.ViewHolder {

        MyBoldTextView CName;
        MyTextView msgTv;
        CircleImageView ProfileDp;
        RelativeLayout ContactLay;
        TextView countTv;
        RelativeLayout msgBadge;

        public Items(View itemView) {
            super(itemView);
            ProfileDp = (CircleImageView) itemView.findViewById(R.id.ContactDp);
            CName = (MyBoldTextView) itemView.findViewById(R.id.ContactName);
            msgTv = (MyTextView) itemView.findViewById(R.id.msgTv);
            ContactLay = (RelativeLayout) itemView.findViewById(R.id.ContactLay);
            countTv = (TextView) itemView.findViewById(R.id.countTv);
            msgBadge = (RelativeLayout) itemView.findViewById(R.id.msgBadge);

            ContactLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    countTv.setText("0");
                    countTv.setVisibility(View.GONE);
                    msgBadge.setVisibility(View.GONE);

                    Intent intent = new Intent(context, ChatsHome.class);
                    IncomeChatHandel.setChatCount(1);
                    Bundle bundle = new Bundle();
                    bundle.putString(ApiKeys.USER_NAME, packageNames.get(getAdapterPosition()).getUName());
                    bundle.putString("FUID", packageNames.get(getAdapterPosition()).getUID());
                    bundle.putString("FImage", packageNames.get(getAdapterPosition()).getUImage());
                    bundle.putString("FEmail", packageNames.get(getAdapterPosition()).getUEmail());
                    bundle.putString(ApiKeys.toUId, UserDetails.getUID());
//                    intent.putExtra("FName", packageNames.get(getAdapterPosition()).getUName());
//                    intent.putExtra("FUID", packageNames.get(getAdapterPosition()).getUID());
//                    intent.putExtra("FImage", packageNames.get(getAdapterPosition()).getUImage());
//                    intent.putExtra("FEmail", packageNames.get(getAdapterPosition()).getUEmail());
                    intent.putExtras(bundle);
                    context.startActivity(intent);


                }
            });

        }
    }

    private void showProfileImage(Bitmap bitmap) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picture_alert_layout);
        dialog.setCancelable(true);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.profileIv);
        imageView.setImageBitmap(bitmap);


        dialog.show();
    }
}