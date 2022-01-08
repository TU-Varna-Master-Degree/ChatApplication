package com.example.myapplication.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewHolderImpl;
import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewType;
import com.example.myapplication.utils.NetClient;

import java.util.List;
import java.util.function.Consumer;

import domain.client.dto.GroupUserDto;
import domain.client.dto.MessageDto;
import domain.client.dto.NotificationDto;
import domain.client.enums.MessageType;

public class ChatAdapter extends RecyclerView.Adapter
{
    final private NotificationDto notifications;
    final private List<GroupUserDto> users;
    final private List<MessageDto> messages;
    final private ChatItemViewHolderFactory factory;
    Consumer<MessageDto> cb;
    
    // SortedSet
    public ChatAdapter(NotificationDto dto, NetClient client, Consumer<MessageDto> cb)
    {
        this.notifications = dto;
        this.users = notifications.getUsers();
        this.messages = notifications.getMessages();
        this.factory = new ChatItemViewHolderFactory(client);
        this.cb = cb;
    }
    
    private boolean impl_detail_is_image(MessageDto msg)
    {
        return true;
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
                {
                    if(impl_detail_is_image(msg))
                    {
                        return ChatItemViewType.VIEW_TYPE_POV_IMAGE.getValue();
                    }
                    return ChatItemViewType.VIEW_TYPE_POV_FILE.getValue();
                }
                
            }
        }
        else
        {
            switch(msg.getMessageType())
            {
                case TEXT:
                    return ChatItemViewType.VIEW_TYPE_OTHER_TEXT.getValue();
                case FILE:
                {
                    if(impl_detail_is_image(msg))
                    {
                        return ChatItemViewType.VIEW_TYPE_POV_IMAGE.getValue();
                    }
                    return ChatItemViewType.VIEW_TYPE_OTHER_FILE.getValue();
                }
                
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
        ChatItemViewHolderImpl binder = (ChatItemViewHolderImpl) holder;
        binder.setMessageContent(dto);
        binder.setUsername(dto.getUsername());
        
        if(dto.getMessageType() == MessageType.FILE)
        {
            holder.itemView.setOnClickListener((v) ->  cb.accept(dto) );
        }
    }
    
    @Override
    public int getItemCount()
    {
        return messages.size();
    }
    
    
    public void insert(MessageDto msg)
    {
        messages.add( msg );
        notifyItemInserted(messages.size() - 1);
    }
    
    public void update(MessageDto data)
    {
        int index = impl_detail_find_and_update_message_list(data);
        if(index == -1)
            return;
        notifyItemChanged(index);
    }
    
    private int impl_detail_find_and_update_message_list(MessageDto dto)
    {
        for(int i = 0; i < messages.size(); i++)
        {
            MessageDto msg = messages.get(i);
            if(msg.getNotificationId().equals(dto.getNotificationId()))
            {
                msg.setContent( dto.getContent() );
                return i;
            }
        }

        return -1;
    }
    
    private boolean isMessageSentByRecipient(MessageDto msg)
    {
        return msg.isOwner();
    };
    
}
