package com.example.myapplication.view;


import domain.client.dto.MessageDto;

public interface IChatViewDataBinder
{
    void setMessageContent(MessageDto data);
    void setUsername(String user);
}
