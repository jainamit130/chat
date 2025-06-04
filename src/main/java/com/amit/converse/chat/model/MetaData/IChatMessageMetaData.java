package com.amit.converse.chat.model.MetaData;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public interface IChatMessageMetaData {
    Map<String, Set<String>> getDeliveryReceiptsByTime();
    Map<String, Set<String>> getReadReceiptsByTime();
    void clearMessageMetadata();
    Integer readMessage(Instant timestamp, String userId);
    Integer deliverMessage(Instant timestamp, String userId);
}
