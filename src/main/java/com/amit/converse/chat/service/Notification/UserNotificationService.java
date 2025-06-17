package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.IUserNotification;
import com.amit.converse.chat.dto.Notification.UserStatusNotification;
import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationService extends NotificationService<IUserNotification> {

    @Autowired
    private ChatNotificationService chatNotificationService;

    public UserNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    @Override
    protected final String getBaseAddress() {
        return "/user";
    }

    @Override
    public void sendNotification(String userId,IUserNotification notification) {
        webSocketMessageService.sendNotification(getAddress(userId),notification);
    }

    public void sendNotificationToUserChats(User user, UserStatusNotification userStatusNotification) {
        for(String chatId:user.getAllChatRoomIds()) {
            userStatusNotification.setChatRoomId(chatId);
            chatNotificationService.sendNotification(chatId, userStatusNotification);
        }
    }

}