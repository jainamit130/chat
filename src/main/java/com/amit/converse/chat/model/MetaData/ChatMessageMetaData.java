package com.amit.converse.chat.model.MetaData;

import lombok.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChatMessageMetaData extends MessageMetaData {

    public ChatMessageMetaData() {
        super();
        this.deletedForEveryone = false;
        this.deliveredRecipients = new HashSet<>();
        this.readRecipients = new HashSet<>();
        this.deliveryReceiptsByTime = new HashMap<>();
        this.readReceiptsByTime = new HashMap<>();
    }

    private Boolean deletedForEveryone;
    private Set<String> deliveredRecipients;
    private Set<String> readRecipients;
    private Map<String, Set<String>> deliveryReceiptsByTime;
    private Map<String, Set<String>> readReceiptsByTime;

    public void clearMessageMetadata() {
        this.deletedForEveryone = true;
        this.deliveredRecipients = new HashSet<>();
        this.readRecipients = new HashSet<>();
        this.deliveryReceiptsByTime = new HashMap<>();
        this.readReceiptsByTime = new HashMap<>();
    }

    @Override
    public Integer readMessage(Instant timestamp,String userId) {
        if(!readRecipients.contains(userId)){
            readRecipients.add(userId);
            Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp.toString(),new HashSet());
            userIds.add(userId);
            readReceiptsByTime.put(timestamp.toString(),userIds);
        }
        return readRecipients.size();
    }

    @Override
    public Integer deliverMessage(Instant timestamp, String userId) {
        if(!deliveredRecipients.contains(userId)) {
            deliveredRecipients.add(userId);
            Set<String> userIds = deliveryReceiptsByTime.getOrDefault(timestamp.toString(), new HashSet());
            userIds.add(userId);
            deliveryReceiptsByTime.put(timestamp.toString(), userIds);
        }
        return deliveredRecipients.size();
    }
}
