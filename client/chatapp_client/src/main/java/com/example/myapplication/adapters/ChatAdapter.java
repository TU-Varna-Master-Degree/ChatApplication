package com.example.myapplication.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.domain.enums.ChatItemViewType;
import com.example.myapplication.domain.enums.MessageType;
import com.example.myapplication.domain.models.GroupUser;
import com.example.myapplication.domain.models.Message;
import com.example.myapplication.domain.models.Notification;
import com.example.myapplication.holders.MessageHolders.MessageHolderFactory;
import com.example.myapplication.holders.MessageHolders.BaseMessage;
import com.example.myapplication.utils.FileSaveDialog;
import com.example.myapplication.utils.DateTimeUtil;
import com.example.myapplication.utils.NetClient;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<BaseMessage> {

    private final Long userId;
    private final List<GroupUser> users;
    private final List<Message> messages;
    private final MessageHolderFactory factory;
    private final FileSaveDialog saveDialog;

    public ChatAdapter(Notification notification, NetClient client, FileSaveDialog saveDialog) {
        this.userId = notification.getUserId();
        this.users = notification.getUsers();
        this.messages = notification.getMessages();
        this.factory = new MessageHolderFactory(client);
        this.saveDialog = saveDialog;
        this.addUserNotifications();
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = messages.get(position);

        if (msg.getMessageType().equals(MessageType.USER_JOIN)) {
            return ChatItemViewType.USER_JOIN.ordinal();
        }

        if (isMessageSentByRecipient(msg)) {
            switch (msg.getMessageType()) {
                case TEXT:
                    return ChatItemViewType.POV_TEXT.ordinal();
                case FILE:
                    return ChatItemViewType.POV_FILE.ordinal();
                case IMAGE:
                    return ChatItemViewType.POV_IMAGE.ordinal();
            }
        } else {
            switch (msg.getMessageType()) {
                case TEXT:
                    return ChatItemViewType.OTHER_TEXT.ordinal();
                case FILE:
                    return ChatItemViewType.OTHER_FILE.ordinal();
                case IMAGE:
                    return ChatItemViewType.OTHER_IMAGE.ordinal();
            }
        }

        // MARK UNREACHABLE
        return -1;
    }

    @NonNull
    @Override
    public BaseMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return factory.create(parent, ChatItemViewType.values()[viewType], saveDialog);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMessage holder, int position) {
        Message message = messages.get(position);
        holder.setMessageContent(message);
        holder.setUsername(message.getUsername());

        LocalDate sendMessageDate = message.getSendDate().toLocalDate();
        if (position != 0 && messages.get(position - 1).getSendDate().toLocalDate().compareTo(sendMessageDate) >= 0) {
            holder.hideDate();
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void addUserNotifications() {
        GroupUser currentUser = users.stream()
            .filter(u -> u.getId().equals(userId))
            .findFirst()
            .orElse(null);

        if (currentUser != null) {
            for (int i = 0; i < users.size(); i++) {
                GroupUser user = users.get(i);
                if (currentUser.getJoinDate().compareTo(user.getJoinDate()) >= 0) {
                    break;
                }
                addNewUserMessage(user);
            }

            messages.sort(Comparator.comparing(Message::getSendDate));
        }
    }

    private void addNewUserMessage(GroupUser user) {
        String joinTime = DateTimeUtil.formatToTime(user.getJoinDate());
        Message newMessage = new Message();
        newMessage.setMessageType(MessageType.USER_JOIN);
        newMessage.setUsername(String.format("%s joined the group at %s.", user.getUsername(), joinTime));
        newMessage.setSendDate(user.getJoinDate());
        messages.add(newMessage);
    }

    public void notifyForNewUserMessage(GroupUser user) {
        addNewUserMessage(user);
        notifyItemInserted(messages.size() - 1);
    }

    public void insert(Message msg) {
        messages.add(msg);
        notifyItemInserted(messages.size() - 1);
    }

    public void update(Message data) {
        int index = findMessageIndex(data);
        if (index != -1) {
            messages.set(index, data);
            notifyItemChanged(index);
        }
    }

    private int findMessageIndex(Message data) {
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (data.getNotificationId().equals(msg.getNotificationId())) {
                return i;
            }
        }
        return -1;
    }

    private boolean isMessageSentByRecipient(Message msg) {
        return msg.isOwner();
    }
}
