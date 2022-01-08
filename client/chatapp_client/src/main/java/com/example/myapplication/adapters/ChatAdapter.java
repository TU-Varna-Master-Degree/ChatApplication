package com.example.myapplication.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.domain.enums.ChatItemViewType;
import com.example.myapplication.domain.enums.MessageType;
import com.example.myapplication.domain.models.GroupUser;
import com.example.myapplication.domain.models.Message;
import com.example.myapplication.domain.models.Notification;
import com.example.myapplication.holders.ChatItemViewHolderFactory;
import com.example.myapplication.holders.ChatItemViewHolderImpl.ChatItemViewHolderImpl;
import com.example.myapplication.utils.NetClient;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class ChatAdapter extends RecyclerView.Adapter
{
    final private Notification notifications;
    final private List<GroupUser> users;
    final private List<Message> messages;
    final private ChatItemViewHolderFactory factory;
    Consumer<Message> cb;

    // SortedSet
    public ChatAdapter(Notification dto, NetClient client, Consumer<Message> cb)
    {
        this.notifications = dto;
        this.users = notifications.getUsers();
        this.messages = notifications.getMessages();
        this.factory = new ChatItemViewHolderFactory(client);
        this.cb = cb;
    }

    private boolean impl_detail_is_image(Message msg)
    {
        return true;
    }
    
    @Override
    public int getItemViewType(int position)
    {
        Message msg = messages.get(position);
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
        Message message = messages.get(position);
        ChatItemViewHolderImpl binder = (ChatItemViewHolderImpl) holder;
        binder.setMessageContent(message);
        binder.setUsername(message.getUsername());

        LocalDate sendMessageDate = message.getSendDate().toLocalDate();
        if (position != 0 && messages.get(position - 1).getSendDate().toLocalDate().compareTo(sendMessageDate) >= 0) {
            binder.hideDate();
        }

//        if (message.isOwner()) {
//            binder.installEditModeBehaviour();
//        }

        if(message.getMessageType() == MessageType.FILE)
        {
            holder.itemView.setOnClickListener((v) ->  cb.accept(message) );
        }
    }
    
    @Override
    public int getItemCount()
    {
        return messages.size();
    }
    
    
    public void insert(Message msg)
    {
        messages.add( msg );
        notifyItemInserted(messages.size() - 1);
    }
    
    public void update(Message data)
    {
        int index = impl_detail_find_and_update_message_list(data);
        if(index == -1)
            return;
        notifyItemChanged(index);
    }
    
    private int impl_detail_find_and_update_message_list(Message dto)
    {
        for(int i = 0; i < messages.size(); i++)
        {
            Message msg = messages.get(i);
            if(msg.getNotificationId().equals(dto.getNotificationId()))
            {
                msg.setContent( dto.getContent() );
                return i;
            }
        }

        return -1;
    }
    
    private boolean isMessageSentByRecipient(Message msg)
    {
        return msg.isOwner();
    };
    
}
