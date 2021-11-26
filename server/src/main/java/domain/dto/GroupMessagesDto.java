package domain.dto;

import domain.enums.MessageType;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.util.List;

public class GroupMessagesDto {
    static public class Message
    {
        private MessageType messageType;
        private String content;
        private LocalDate sendDate;
        private String filePath;
        private boolean received;
        private String senderUsername;

        public Message(MessageType messageType, String content, LocalDate sendDate, String filePath, boolean received, String senderUsername) {
            this.messageType = messageType;
            this.content = content;
            this.sendDate = sendDate;
            this.filePath = filePath;
            this.received = received;
            this.senderUsername = senderUsername;
        }

        public MessageType getMessageType() {
            return messageType;
        }

        public void setMessageType(MessageType messageType) {
            this.messageType = messageType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public LocalDate getSendDate() {
            return sendDate;
        }

        public void setSendDate(LocalDate sendDate) {
            this.sendDate = sendDate;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public boolean isReceived() {
            return received;
        }

        public void setReceived(boolean received) {
            this.received = received;
        }

        public String getSenderUsername() {
            return senderUsername;
        }

        public void setSenderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
        }
    }

    private String groupName;
    private String groupCreateDate;
    private List<Message> messages;

    public GroupMessagesDto(String groupName, String groupCreateDate, List<Message> messages) {
        this.groupName = groupName;
        this.groupCreateDate = groupCreateDate;
        this.messages = messages;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCreateDate() {
        return groupCreateDate;
    }

    public void setGroupCreateDate(String groupCreateDate) {
        this.groupCreateDate = groupCreateDate;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
