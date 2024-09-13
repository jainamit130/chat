package com.amit.converse.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMessageStats {
    private int sentMessages;
    private int deliveredMessages;
    private int readMessages;

    public void incrementSentMessages() {
        this.sentMessages++;
    }

    public int getDeliveredMessages() {
        return deliveredMessages;
    }

    public void incrementDeliveredMessages() {
        this.deliveredMessages++;
    }

    public void incrementReadMessages() {
        this.readMessages++;
    }
}
