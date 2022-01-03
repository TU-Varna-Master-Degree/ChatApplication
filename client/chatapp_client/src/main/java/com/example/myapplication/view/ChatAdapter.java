package com.example.myapplication.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import domain.client.dto.GroupMessageDto;
import domain.client.dto.GroupUserDto;
import domain.client.dto.MessageDto;
import domain.client.dto.NotificationDto;

public class ChatAdapter extends RecyclerView.Adapter
{
    final private ChatItemViewHolderFactory factory = new ChatItemViewHolderFactory();
    final private NotificationDto notifications;
    final private List<GroupUserDto> users;
    final private List<MessageDto> messages;
    
    public ChatAdapter(NotificationDto dto)
    {
        this.notifications = dto;
        this.users = notifications.getUsers();
        this.messages = notifications.getMessages();
    }
    
    @Override
    public int getItemViewType(int position)
    {
        MessageDto msg = messages.get(position);
        if( isMessageSentByRecipient(msg) )
        {
            switch(msg.getMessageType())
            {
                case TEXT:
                    return ChatItemViewType.VIEW_TYPE_POV_TEXT.getValue();
                case FILE:
                    return ChatItemViewType.VIEW_TYPE_POV_FILE.getValue();
            }
        }
        else
        {
            switch(msg.getMessageType())
            {
                case TEXT:
                    return ChatItemViewType.VIEW_TYPE_OTHER_TEXT.getValue();
                case FILE:
                    return ChatItemViewType.VIEW_TYPE_OTHER_FILE.getValue();
            }
        }
        
        // MARK UNREACHABLE
        assert false;
        return -1;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return factory.create(parent, ChatItemViewType.values()[viewType]);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        MessageDto dto = messages.get(position);
        IChatViewDataBinder binder = (IChatViewDataBinder) holder;
        binder.setMessageContent(dto);
        binder.setUsername(dto.getUsername());
    }
    
    @Override
    public int getItemCount()
    {
        return messages.size();
    }
    
    
    public void insert(GroupMessageDto msg)
    {
        MessageDto dto = impl_detail_from_group_msg(msg);
        messages.add( dto );
        
        notifyItemInserted(messages.size() - 1);
    }
    
    public void update(GroupMessageDto data)
    {
        int index = impl_detail_find_and_update_message_list(data);
        if(index == -1)
            return;
        notifyItemChanged(index);
    }
    
    private int impl_detail_find_and_update_message_list(GroupMessageDto dto)
    {
        for(int i = 0; i < messages.size(); i++)
        {
            MessageDto msg = messages.get(i);
            if(msg.getNotificationId().equals(dto.getMessageId()))
            {
                msg.setContent( dto.getContent() );
                return i;
            }
        }
        
        return -1;
    }
    
    private MessageDto impl_detail_from_group_msg(GroupMessageDto dto)
    {
        return new MessageDto(
                dto.getMessageId(),
                dto.getContent(),
                dto.getMessageType(),
                dto.getSendDate(),
                null,
                null,
                null,
                dto.getUserId(),
                dto.getUsername(),
                true);
                
    }
    
    private boolean isMessageSentByRecipient(MessageDto msg)
    {
        return msg.isOwner();
    };
    
}
