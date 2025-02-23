package com.amit.converse.chat.model.Messages;

import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.MetaData.ChatMessageMetaData;
import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "messages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public class ChatMessage extends Message {
    private String senderId;
    private MessageStatus status;

    public ChatMessage() {
        super("There are no messages!",new ChatMessageMetaData());
    }

    public ChatMessage(String senderId,String content) {
        super(content,new ChatMessageMetaData());
        this.senderId = senderId;
        this.status = MessageStatus.PENDING;
    }

    public void setMessageStatus(MessageStatus newStatus) {
        status = newStatus;
    }

    public void readMessage() { this.status=MessageStatus.READ; }

    public void deliverMessage() { this.status=MessageStatus.DELIVERED; }

    @Override
    public Integer readMessage(String timestamp,String userId) {
        return messageMetaData.readMessage(timestamp,userId);
    }

    @Override
    public Integer deliverMessage(String timestamp,String userId) {
        return messageMetaData.deliverMessage(timestamp,userId);
    }
}
