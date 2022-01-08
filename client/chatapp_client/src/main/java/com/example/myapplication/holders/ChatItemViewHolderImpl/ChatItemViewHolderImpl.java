package com.example.myapplication.holders.ChatItemViewHolderImpl;

import com.example.myapplication.domain.models.Message;

public interface ChatItemViewHolderImpl {

    void hideDate();

    void setMessageContent(Message data);

    void setUsername(String user);
}
