package com.amit.converse.chat.dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatTransactionNotification extends IChatNotification {
    private List<TransactionNotification> notifications;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.TRANSACTION;
    }
}
