package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.holders.ChatItemViewHolder;
import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewHolderImpl;
import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewType;
import com.example.myapplication.holders.ChatItemViewHolderImpl.FileMessage;
import com.example.myapplication.holders.ChatItemViewHolderImpl.TextMessage;
import com.example.myapplication.utils.NetClient;

import domain.client.dialogue.ServerRequest;
import domain.client.dto.SendMessageDto;
import domain.client.enums.MessageType;
import domain.client.enums.OperationType;

public class ChatItemViewHolderFactory
{
    NetClient client;
    public ChatItemViewHolderFactory( NetClient client )
    {
        this.client = client;
    }
    
    RecyclerView.ViewHolder create(ViewGroup parent, ChatItemViewType type)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = ChatItemViewType.isOwnerPov(type) ?
                createSentMessageView(inflater, parent) :
                createReceiveMessageView(inflater, parent);
        
        ChatItemViewHolderImpl binder = null;
       
        switch( type )
        {
            case VIEW_TYPE_POV_TEXT:
            case VIEW_TYPE_OTHER_TEXT:
                ((RelativeLayout)view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_text_container, null)
                );
                
                binder = new TextMessage(view,  (id, newContent)->
                {
                    ServerRequest<SendMessageDto> req = new ServerRequest<>(OperationType.EDIT_NOTIFICATION);
                    {
                        SendMessageDto sendMessageDto = new SendMessageDto();
                        sendMessageDto.setMessageId( id );
                        sendMessageDto.setMessageType(MessageType.TEXT);
                        sendMessageDto.setContent(newContent);
                        req.setData(sendMessageDto);
                    }
                    
                    client.sendRequest(req);
                
                });
                
                break;
            case VIEW_TYPE_POV_FILE:
            case VIEW_TYPE_OTHER_FILE:
                ((RelativeLayout)view.findViewById(R.id.chat_item_card_view)).addView(
                        inflater.inflate(R.layout.chat_item_file_container, null)
                );
                binder = new FileMessage(view);
                
                break;
            case VIEW_TYPE_POV_IMAGE:
            case VIEW_TYPE_OTHER_IMAGE:
                return null;
        }
  
        return new ChatItemViewHolder( view, binder );
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
