package com.example.myapplication.holders.ChatItemViewHolderImpl;

import domain.client.dto.MessageDto;

public interface ChatItemViewHolderImpl
{
    void setMessageContent(MessageDto data);
    void setUsername(String user);
}
