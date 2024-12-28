package com.amit.converse.chat.model;

import com.amit.converse.chat.model.MetaData.ChatMessageMetaData;
import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends Message {
    private String senderId;
    private MessageStatus status;

    @Builder.Default
    private MessageMetaData messageMetaData = new ChatMessageMetaData();

    public void setMessageStatus(MessageStatus newStatus) {
        status = newStatus;
    }

    @Override
    public void readMessage(String timestamp,String userId) {
        messageMetaData.readMessage(timestamp,userId);
    }

    @Override
    public void deliverMessage(String timestamp,String userId) {
        messageMetaData.deliverMessage(timestamp,userId);
    }
}
