package com.amit.converse.chat.model.Messages;

import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.MetaData.ChatMessageMetaData;
import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "messages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public class ChatMessage extends Message implements IDeletableMessage {
    private String senderId;
    private MessageStatus status;
    private String name;
    private Boolean deletedForEveryone;

    public ChatMessage() {
        super("There are no messages!",new ChatMessageMetaData());
        this.deletedForEveryone = false;
    }

    public ChatMessage(Instant timestamp) {
        super("There are no messages!",new ChatMessageMetaData(),timestamp);
        this.deletedForEveryone = false;
    }

    public ChatMessage(String senderId,String content) {
        super(content,new ChatMessageMetaData());
        this.senderId = senderId;
        this.status = MessageStatus.PENDING;
        this.deletedForEveryone = false;
    }

    public void setMessageStatus(MessageStatus newStatus) {
        status = newStatus;
    }

    public void readMessage() { this.status=MessageStatus.READ; }

    public void deliverMessage() { this.status=MessageStatus.DELIVERED; }

    @Override
    public Integer readMessage(Instant timestamp,String userId) {
        return messageMetaData.readMessage(timestamp,userId);
    }

    @Override
    public Integer deliverMessage(Instant timestamp, String userId) {
        return messageMetaData.deliverMessage(timestamp,userId);
    }

    @Override
    public void deleteForEveryone() {
        this.content = "This message was deleted!";
        this.status = MessageStatus.DELETED;
        this.deletedForEveryone = true;
        messageMetaData.clearMessageMetadata();
    }


    public Map<String, Set<String>> getDeliveryReceiptsByTime() {
        return messageMetaData.getDeliveryReceiptsByTime();
    }

    public Map<String, Set<String>> getReadReceiptsByTime() {
        return messageMetaData.getReadReceiptsByTime();
    }
}
