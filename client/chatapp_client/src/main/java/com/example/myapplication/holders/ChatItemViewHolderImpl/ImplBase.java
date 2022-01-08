package com.example.myapplication.holders.ChatItemViewHolderImpl;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.domain.models.Message;
import com.example.myapplication.utils.DateTimeUtil;

import java.time.LocalDateTime;

public class ImplBase
        implements ChatItemViewHolderImpl {
    protected TextView tvDate, tvTime, tvUsername;
    protected View view;
    protected Message data;

    public ImplBase(View view) {
        this.view = view;
        bindViews(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setMessageContent(Message data) {
        this.data = data;

        LocalDateTime dt = data.getSendDate();
        tvTime.setText(DateTimeUtil.formatToTime(dt));
        tvDate.setText(DateTimeUtil.formatToDate(dt));

        tvTime.setVisibility(View.VISIBLE);
        tvDate.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUsername(String user) {
        tvUsername.setText(user);
    }

    private void bindViews(View itemView) {
        tvDate = itemView.findViewById(R.id.chat_item_tv_date);
        tvTime = itemView.findViewById(R.id.chat_item_tv_time);
        tvUsername = itemView.findViewById(R.id.chat_item_tv_name);
    }

    public void hideDate() {
        tvDate.setVisibility(View.GONE);
    }

    public Message getData() {
        return data;
    }

}
