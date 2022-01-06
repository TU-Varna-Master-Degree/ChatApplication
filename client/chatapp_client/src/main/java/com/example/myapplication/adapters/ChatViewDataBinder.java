package com.example.myapplication.adapters;

import java.util.function.Consumer;

import domain.client.dto.MessageDto;

public interface ChatViewDataBinder {

    void setMessageContent(MessageDto data);
    void setUsername(String user);
    void installEditModeBehaviour(Consumer<String> editChange );
}
