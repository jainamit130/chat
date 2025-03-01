package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.IUserNotification;
import com.amit.converse.chat.dto.Notification.UserOnlineStatusNotification;
import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationService extends NotificationService {

    @Autowired
    private ChatNotificationService chatNotificationService;

    public UserNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    @Override
    protected final String getBaseAddress() {
        return "/user";
    }

    public void sendNotification(String userId,IUserNotification notification) {
        webSocketMessageService.sendNotification(getAddress(userId),notification);
    }

    public void sendNotificationToUserChats(User user, UserOnlineStatusNotification userOnlineStatusNotification) {
        for(String chatId:user.getChatRoomIds()) {
            chatNotificationService.sendNotification(chatId, userOnlineStatusNotification);
        }
    }

}