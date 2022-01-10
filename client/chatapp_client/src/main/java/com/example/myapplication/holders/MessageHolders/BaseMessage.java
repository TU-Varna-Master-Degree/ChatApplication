package com.example.myapplication.holders.MessageHolders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.models.Message;
import com.example.myapplication.utils.DateTimeUtil;

import java.time.LocalDateTime;

public class BaseMessage extends RecyclerView.ViewHolder {

    protected TextView tvDate, tvTime, tvUsername;
    protected View view;
    protected Message data;

    public BaseMessage(View view) {
        super(view);
        this.view = view;
        bindViews(view);
    }

    @SuppressLint("DefaultLocale")
    public void setMessageContent(Message data) {
        this.data = data;
        LocalDateTime dt = data.getSendDate();

        if (tvTime != null) {
            tvTime.setText(DateTimeUtil.formatToTime(dt));
            tvTime.setVisibility(View.VISIBLE);
        }

        tvDate.setText(DateTimeUtil.formatToDate(dt));
        tvDate.setVisibility(View.VISIBLE);
    }

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
