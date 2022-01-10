package com.example.myapplication.holders.MessageHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.enums.ChatItemViewType;
import com.example.myapplication.domain.enums.MessageType;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.models.SendMessage;
import com.example.myapplication.utils.FileSaveDialog;
import com.example.myapplication.utils.NetClient;

public class MessageHolderFactory {

    NetClient client;

    public MessageHolderFactory(NetClient client) {
        this.client = client;
    }

    public BaseMessage create(ViewGroup parent, ChatItemViewType type, FileSaveDialog saveDialog) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (type.equals(ChatItemViewType.USER_JOIN)) {
            View view = inflater.inflate(R.layout.chat_view_user_text, parent, false);
            return new BaseMessage(view);
        }

        View view = type.isOwnerPov() ?
                createSentMessageView(inflater, parent) :
                createReceiveMessageView(inflater, parent);

        int boxColor = ContextCompat.getColor(parent.getContext(),
                type.isOwnerPov() ? R.color.purple_200 : R.color.white);
        BaseMessage binder = null;

        switch (type) {
            case POV_TEXT: {
                ((RelativeLayout) view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_text_container, null));
                binder = new TextMessage(view, this::sendEditRequest);
                break;
            }
            case OTHER_TEXT: {
                ((RelativeLayout) view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_text_container, null));
                binder = new TextMessage(view);
                break;
            }
            case POV_FILE:
            case OTHER_FILE: {
                ((RelativeLayout) view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_file_container, null));
                binder = new FileMessage(boxColor, view, this::loadNotificationContent);
                break;
            }
            case POV_IMAGE:
            case OTHER_IMAGE: {
                ((RelativeLayout) view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_file_container, null));
                binder = new ImageMessage(boxColor, view, this::loadNotificationContent, saveDialog);
                break;
            }
        }

        return binder;
    }

    private void sendEditRequest(Long messageId, String content) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setMessageId(messageId);
        sendMessage.setMessageType(MessageType.TEXT);
        sendMessage.setContent(content);

        ServerRequest<SendMessage> req = new ServerRequest<>(OperationType.EDIT_NOTIFICATION);
        req.setData(sendMessage);
        client.sendRequest(req);
    }

    private void loadNotificationContent(Long id) {
        ServerRequest<Long> serverRequest = new ServerRequest<>();
        serverRequest.setOperationType(OperationType.GET_NOTIFICATION);
        serverRequest.setData(id);
        client.sendRequest(serverRequest);
    }

    private View createSentMessageView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.chat_view_item_send_text, parent, false);
    }

    private View createReceiveMessageView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.chat_view_item_receive_text, parent, false);
    }
}
