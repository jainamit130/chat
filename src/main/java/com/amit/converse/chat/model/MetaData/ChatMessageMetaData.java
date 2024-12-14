package com.amit.converse.chat.model.MetaData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
