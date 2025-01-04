package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.INotification;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebSocketMessageService {

    private static final String address = "/topic";
    private final SimpMessagingTemplate messagingTemplate;

    private String getAddress(String topicId) {
        return address+topicId;
    }

    public void sendNotification(String addressId,INotification notification) {
        messagingTemplate.convertAndSend(getAddress(addressId),notification);
    }
}
