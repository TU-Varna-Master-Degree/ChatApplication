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
    private static final int MESSAGE_INSERT_INDEX = 0;
    
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
        if( impl_detail_cmp_id_with_own(msg) )
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
        
        // Find user
        for(GroupUserDto obj : users)
        {
            if(impl_detail_get_long_id(dto).equals(obj.getId()))
            {
                binder.setMessageUserData( obj );
                break;
            }
        }

        binder.setMessageContent(dto);

    }
    
    @Override
    public int getItemCount()
    {
        return messages.size();
    }
    
    
    public void insert(GroupMessageDto msg)
    {
        MessageDto dto = impl_detail_from_group_msg(msg);
        messages.add(MESSAGE_INSERT_INDEX, dto);
        
        notifyItemInserted(MESSAGE_INSERT_INDEX);
    }
    
    public void update(GroupMessageDto data)
    {
        int index = impl_detail_find_and_update_message_list(data);
        notifyItemChanged(index);
    }
    
    private int impl_detail_find_and_update_message_list(GroupMessageDto dto)
    {
        // TODO: Implement
        assert false;
        return 0;
    }
    
    private MessageDto impl_detail_from_group_msg(GroupMessageDto dto)
    {
        // TODO: Implement
        assert false;
        return null;
    }
    
    private boolean impl_detail_cmp_id_with_own(MessageDto msg)
    {
        // TODO: Implement
        assert false;
        return impl_detail_get_long_id(msg).equals( impl_detail_get_own_id());
    };
    
    private Long impl_detail_get_long_id(MessageDto msg)
    {
        // TODO: Implement
        assert false;
        return null;
    }
    
    private Long impl_detail_get_own_id()
    {
        // TODO: Implement
        assert false;
        return null;
    }
    
    
}
