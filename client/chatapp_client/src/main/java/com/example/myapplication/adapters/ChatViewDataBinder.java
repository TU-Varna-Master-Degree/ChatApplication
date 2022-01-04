package com.example.myapplication.adapters;

import domain.client.dto.MessageDto;

public interface ChatViewDataBinder {

    void setMessageContent(MessageDto data);
    void setUsername(String user);
    void installEditModeBehaviour();
}
