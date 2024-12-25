package com.amit.converse.chat.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class NotificationMessage extends Message {

    @Override
    public void readMessage(String timestamp, String userId) {

    }

    @Override
    public void deliverMessage(String timestamp, String userId) {

    }
}
