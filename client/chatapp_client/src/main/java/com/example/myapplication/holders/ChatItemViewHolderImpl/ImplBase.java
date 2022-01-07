package com.example.myapplication.holders.ChatItemViewHolderImpl;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

import java.time.LocalDateTime;

import domain.client.dto.MessageDto;

public class ImplBase
    implements ChatItemViewHolderImpl
{
    protected TextView tvDate, tvTime, tvUsername;
    protected View view;
    protected MessageDto data;
    
    public ImplBase(View view)
    {
        this.view = view;
        bindViews(view);
    }
    
    @SuppressLint("DefaultLocale")
    @Override
    public void setMessageContent(MessageDto data)
    {
        this.data = data;
        
        LocalDateTime dt = data.getSendDate();
        tvTime.setText( String.format("%2d:%2d", dt.getHour(), dt.getMinute()) );
        tvDate.setText( String.format("%2d/%2d/%4d", dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()) );
        tvTime.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void setUsername(String user)
    {
        tvUsername.setText(user);
    }
    
    private void bindViews(View itemView)
    {
        tvDate = itemView.findViewById(R.id.chat_item_tv_date);
        tvTime = itemView.findViewById(R.id.chat_item_tv_time);
        tvUsername = itemView.findViewById(R.id.chat_item_tv_name);
    }
    
    public void showTime()
    {
        tvTime.setVisibility(View.VISIBLE);
    }
    
    public void showDate()
    {
        tvDate.setVisibility(View.VISIBLE);
    }
    
    public void showUser()
    {
        tvUsername.setVisibility(View.VISIBLE);
    }

    
    public MessageDto getData()
    {
        return data;
    }
    
}
