package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.Messages.ChatMessage;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class MarkingContext {
    private Map<String, List<String>> senderSpecificMessageIds = new HashMap<>();
    private List<ChatMessage> markedMessages = new ArrayList<>();
    private List<String> onlineUserIds = new ArrayList<>();

    public Map<String, List<String>> getSenderSpecificMessageIds() {
        return senderSpecificMessageIds;
    }

    public List<ChatMessage> getMarkedMessages() {
        return markedMessages;
    }

    public List<String> getOnlineUserIds() {
        return onlineUserIds;
    }

}
