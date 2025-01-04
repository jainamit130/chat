package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.INotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class NotificationService {
    protected final WebSocketMessageService webSocketMessageService;
    protected abstract String getBaseAddress();

    protected String getAddress(String id) {
        return getBaseAddress() + "/" + id + "/";
    }

    protected abstract INotification getNotification();
}
