package com.amit.converse.chat.model.Messages;

import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.MetaData.ChatMessageMetaData;
import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Document(collection = "messages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public class ChatMessage extends Message {
    private String senderId;
    private MessageStatus status;

    @Builder.Default
    private MessageMetaData messageMetaData = new ChatMessageMetaData();

    public void setMessageStatus(MessageStatus newStatus) {
        status = newStatus;
    }

    @Override
    public Integer readMessage(String timestamp,String userId) {
        return messageMetaData.readMessage(timestamp,userId);
    }

    @Override
    public Integer deliverMessage(String timestamp,String userId) {
        return messageMetaData.deliverMessage(timestamp,userId);
    }
}
