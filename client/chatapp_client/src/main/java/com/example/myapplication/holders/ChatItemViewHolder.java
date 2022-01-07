package com.example.myapplication.holders;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewHolderImpl;

import domain.client.dto.MessageDto;

public class ChatItemViewHolder extends RecyclerView.ViewHolder
    implements ChatItemViewHolderImpl
{

    ChatItemViewHolderImpl impl;
    
    public ChatItemViewHolder(View view, ChatItemViewHolderImpl binderImpl)
    {
        super(view);
        this.impl = binderImpl;
    }
    
    @SuppressLint("DefaultLocale")
    @Override
    public void setMessageContent(MessageDto data)
    {
        impl.setMessageContent(data);
    }
    
    @Override
    public void setUsername(String username)
    {
        impl.setUsername(username);
    }
}
