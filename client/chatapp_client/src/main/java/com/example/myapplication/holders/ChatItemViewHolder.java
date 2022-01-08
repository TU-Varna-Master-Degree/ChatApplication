package com.example.myapplication.holders;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.domain.models.Message;
import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewHolderImpl;

public class ChatItemViewHolder extends RecyclerView.ViewHolder
    implements ChatItemViewHolderImpl
{

    ChatItemViewHolderImpl impl;
    
    public ChatItemViewHolder(View view, ChatItemViewHolderImpl binderImpl)
    {
        super(view);
        this.impl = binderImpl;
    }

    @Override
    public void hideDate() {
        impl.hideDate();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setMessageContent(Message data)
    {
        impl.setMessageContent(data);
    }
    
    @Override
    public void setUsername(String username)
    {
        impl.setUsername(username);
    }
}
