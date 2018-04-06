package com.linkzone.linkzoneapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linkzone.linkzoneapp.DataHolders.ChatGetSetData;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.FCMChat.ApiKeys;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Items> {
    Context context;
    ArrayList<ChatGetSetData> packageNames;

    public ChatAdapter(Context context, ArrayList<ChatGetSetData> packageNames) {
        this.context = context;
        this.packageNames = packageNames;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if(viewType==1)
        {
           v= LayoutInflater.from(context).inflate(R.layout.chat_outgoingmsj, parent, false);
        }
        else if(viewType==2)
        {
            v= LayoutInflater.from(context).inflate(R.layout.chat_outgoingimg, parent, false);
        }
        else if(viewType ==3)
        {
           v= LayoutInflater.from(context).inflate(R.layout.chat_incomingmsj, parent, false);
        }
        else
        {
            v= LayoutInflater.from(context).inflate(R.layout.chat_incomingimg, parent, false);
        }
        return new Items(v);
    }

    @Override
    public int getItemViewType(int position) {
        if(UserDetails.getUID().equalsIgnoreCase(packageNames.get(position).getUserChatToId())&&packageNames.get(position).getUserChatType().equalsIgnoreCase(ApiKeys.MSG_TYP))
        return 1;

        else if(UserDetails.getUID().equalsIgnoreCase(packageNames.get(position).getUserChatToId())&&packageNames.get(position).getUserChatType().equalsIgnoreCase(ApiKeys.IMG_TYP))

        return 2;

        else if(UserDetails.getUID()!=packageNames.get(position).getUserChatToId()&&packageNames.get(position).getUserChatType().equalsIgnoreCase(ApiKeys.MSG_TYP))

            return 3;
        else //if(UserDetails.getUID()!=packageNames.get(position).getUserChatToId()&&packageNames.get(position).getUserChatType().equalsIgnoreCase(ApiKeys.IMG_TYP))
            return 4;

    }

    @Override
    public void onBindViewHolder(final Items holder, final int position) {
        ChatGetSetData chatGetSetData = packageNames.get(position);
        if(packageNames.get(position).getUserChatType().equalsIgnoreCase(ApiKeys.MSG_TYP))
        {
            holder.ChatMessage.setText(chatGetSetData.getUserChatMsg());
        }
        else
        {
            Picasso.with(context)
                    .load("http://xperiaindia.com/linkZone/ChatImages/"+packageNames.get(position).getUserChatImage())
                    .into(holder.ChatImg);
            Log.e("Img Path","http://xperiaindia.com/linkZone/ChatImages/"+packageNames.get(position).getUserChatImage());
        }

    }


    @Override
    public int getItemCount() {
        return packageNames.size();
    }

    public class Items extends RecyclerView.ViewHolder {

        MyTextView ChatMessage;
        ImageView ChatImg;
        public Items(View itemView) {
            super(itemView);
            if(itemView.getId()==R.id.TextType)
            {
                ChatMessage=(MyTextView)itemView.findViewById(R.id.ChatMsj);
            }
            else
            {
                ChatImg=(ImageView)itemView.findViewById(R.id.Chat_Image);

                ChatImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://xperiaindia.com/linkZone/ChatImages/"+packageNames.get(getAdapterPosition()).getUserChatImage()));
                        context.startActivity(browserIntent);
                    }
                });

            }


        }

    }
}