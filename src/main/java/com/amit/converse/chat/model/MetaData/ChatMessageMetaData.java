package com.amit.converse.chat.model.MetaData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageMetaData extends MessageMetaData {
    @Builder.Default
    private Boolean deletedForEveryone = false;

    @Builder.Default
    private Set<String> deliveredRecipients = new HashSet<>();

    @Builder.Default
    private Set<String> readRecipients = new HashSet<>();

    @Builder.Default
    private Map<String, Set<String>> deliveryReceiptsByTime = new HashMap<>();

    @Builder.Default
    private Map<String, Set<String>> readReceiptsByTime = new HashMap<>();


    @Override
    public void readMessage(String timestamp,String userId) {
        if(!readRecipients.contains(userId)){
            readRecipients.add(userId);
            Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp,new HashSet());
            userIds.add(userId);
            readReceiptsByTime.put(timestamp,userIds);
        }
    }

    @Override
    public void deliverMessage(String timestamp, String userId) {
        if(!deliveredRecipients.contains(userId)) {
            deliveredRecipients.add(userId);
            Set<String> userIds = deliveryReceiptsByTime.getOrDefault(timestamp, new HashSet());
            userIds.add(userId);
            deliveryReceiptsByTime.put(timestamp, userIds);
        }
        return;
    }
}
