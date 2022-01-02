package com.example.myapplication.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import domain.client.dto.GroupMessageDto;
import domain.client.dto.GroupUserDto;

public class ChatAdapter extends RecyclerView.Adapter
{
    final private Long povUserId;

    final private List<GroupMessageDto> messages;
    final private List<GroupUserDto> users;
    final private ChatItemViewHolderFactory factory = new ChatItemViewHolderFactory();
    
    public ChatAdapter(Long povUserId, List<GroupMessageDto> messages, List<GroupUserDto> users)
    {
        this.povUserId = povUserId;
        this.messages = messages;
        this.users = users;
    }
    
    @Override
    public int getItemViewType(int position)
    {
        GroupMessageDto msg = messages.get(position);
        if(msg.getUserId().equals(povUserId))
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
        GroupMessageDto dto = messages.get(position);
        IChatViewDataBinder binder = (IChatViewDataBinder) holder;
        
        // Find user
        for(GroupUserDto obj : users)
        {
            if(dto.getUserId().equals(obj.getId()))
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
}
