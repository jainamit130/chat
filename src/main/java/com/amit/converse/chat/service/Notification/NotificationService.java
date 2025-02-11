package com.amit.converse.chat.service.Notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class NotificationService {
    protected final WebSocketMessageService webSocketMessageService;
    protected abstract String getBaseAddress();
    protected String getAddress(String id) {
        return getBaseAddress() + "/" + id;
    }
}
