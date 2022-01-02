package com.example.myapplication.view;


import domain.client.dto.GroupUserDto;
import domain.client.dto.MessageDto;

public interface IChatViewDataBinder
{
    void setMessageContent(MessageDto data);
    void setMessageUserData(GroupUserDto user);
}
