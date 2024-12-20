package com.amit.converse.chat.model;

import com.amit.converse.chat.model.MetaData.ChatMessageMetaData;
import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends Message {
    private MessageStatus status;

    @Builder.Default
    private MessageMetaData messageMetaData = new ChatMessageMetaData();

    public void readMessage(String userId){
        messageMetaData.addUserToReadReceipt
        return;
    }

    @Override
    public void deliverMessage(String userId) {

    }

    public void addUserToReadReceipt(String timestamp,String userId){
        if(!readRecipients.contains(userId)){
            readRecipients.add(userId);
            Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp,new HashSet());
            userIds.add(userId);
            readReceiptsByTime.put(timestamp,userIds);
        }
        return;
    }

    public void addUserToDeliveredReceipt(String timestamp,String userId){
        if(!deliveredRecipients.contains(userId)) {
            deliveredRecipients.add(userId);
            Set<String> userIds = deliveryReceiptsByTime.getOrDefault(timestamp, new HashSet());
            userIds.add(userId);
            deliveryReceiptsByTime.put(timestamp, userIds);
        }
        return;
    }

    public void setMessageStatus(MessageStatus newStatus) {
        status = newStatus;
    }
}
