package com.example.myapplication.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.enums.ChatItemViewType;
import com.example.myapplication.domain.enums.MessageType;
import com.example.myapplication.domain.enums.OperationType;
import com.example.myapplication.domain.models.SendMessage;
import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewHolderImpl;
import com.example.myapplication.holders.ChatItemViewHolderImpl.FileMessage;
import com.example.myapplication.holders.ChatItemViewHolderImpl.ImageMessage;
import com.example.myapplication.holders.ChatItemViewHolderImpl.TextMessage;
import com.example.myapplication.utils.NetClient;

public class ChatItemViewHolderFactory
{
    NetClient client;
    public ChatItemViewHolderFactory( NetClient client )
    {
        this.client = client;
    }

    public RecyclerView.ViewHolder create(ViewGroup parent, ChatItemViewType type)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = ChatItemViewType.isOwnerPov(type) ?
                createSentMessageView(inflater, parent) :
                createReceiveMessageView(inflater, parent);

        ChatItemViewHolderImpl binder = null;

        switch( type )
        {
            case VIEW_TYPE_POV_TEXT:
            {
                ((RelativeLayout)view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_text_container, null)
                );
                binder = new TextMessage(view,  (id, newContent)->
                {
                    ServerRequest<SendMessage> req = new ServerRequest<>(OperationType.EDIT_NOTIFICATION);
                    {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setMessageId( id );
                        sendMessage.setMessageType(MessageType.TEXT);
                        sendMessage.setContent(newContent);
                        req.setData(sendMessage);
                    }

                    client.sendRequest(req);

                });
                break;
            }
            case VIEW_TYPE_OTHER_TEXT:
            {
                ((RelativeLayout)view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_text_container, null)
                );
                binder = new TextMessage(view);
                break;
            }
            case VIEW_TYPE_POV_FILE:
            case VIEW_TYPE_OTHER_FILE:
                ((RelativeLayout)view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_file_container, null)
                );
                binder = new FileMessage(view, this::sendDownloadRequest );
                break;
            case VIEW_TYPE_POV_IMAGE:
            case VIEW_TYPE_OTHER_IMAGE:
                ((RelativeLayout)view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_file_container, null)
                );

                binder = new ImageMessage(view, this::sendDownloadRequest, this::loadImageContent);
        }

        return new ChatItemViewHolder( view, binder );
    }

    void sendDownloadRequest(Long id)
    {
        ServerRequest<Long> request =
                new ServerRequest<>(OperationType.CREATE_NOTIFICATION  /*TODO: GET_FILE_CONTENT*/ );
        {
            request.setData(id);
        }

        client.sendRequest( request );
    }

    void loadImageContent(Long id, ImageView view)
    {
        sendDownloadRequest(id);


    }

    private View createSentMessageView(LayoutInflater inflater, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.chat_view_item_send_text, parent, false);
        return view;
    }

    private View createReceiveMessageView(LayoutInflater inflater, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.chat_view_item_receive_text, parent, false);
        return view;
    }
}
