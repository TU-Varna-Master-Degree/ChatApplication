package com.example.myapplication.view;

import domain.client.dto.GroupMessageDto;
import domain.client.dto.GroupUserDto;

public interface IChatViewDataBinder
{
    void setMessageContent(GroupMessageDto data);
    void setMessageUserData(GroupUserDto user);
}
