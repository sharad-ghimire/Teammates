package com.sharad.teammates.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharad.teammates.R;
import com.sharad.teammates.models.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>  {

    private final Context mContext;
    private final List<Chat> chat;

    public ChatAdapter(Context mContext, List<Chat> chat) {
        this.mContext = mContext;
        this.chat = chat;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_msg_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder viewHolder, int i) {
        Chat myChat =  chat.get(i);
        viewHolder.messageSend.setText(myChat.getMessage());
        if(myChat.getWhichUser()){
            viewHolder.messageSend.setGravity(Gravity.END);
            viewHolder.messageSend.setTextColor(Color.parseColor("#404040"));
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
        } else {
            viewHolder.messageSend.setGravity(Gravity.START);
            viewHolder.messageSend.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#F7C44C37"));
        }


    }

    @Override
    public int getItemCount() {
        return chat.size();
    }


    //Initialze views in here
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageSend;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSend = itemView.findViewById(R.id.msgText);
            linearLayout = itemView.findViewById(R.id.containerLinear);

        }
    }
}
