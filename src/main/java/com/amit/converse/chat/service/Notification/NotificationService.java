package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

    private final WebSocketMessageService webSocketMessageService;


}
