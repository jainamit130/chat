package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NotifyGroupJoin {

    private final WebSocketMessageService webSocketMessageService;

    private String getMessage(User addedByUser, User joinedUser) {
        return addedByUser.getUsername() + " added by " + joinedUser.getUsername();
    }

    public void notifyGroup(User addedByUser, List<User> joinedUsers, String chatRoomId) {
        List<String> joinNotifications = new ArrayList<>();
        for(User joinedUser : joinedUsers) {
            joinNotifications.add(getMessage(addedByUser,joinedUser));
        }
        webSocketMessageService.sendJoinNotification(joinNotifications,chatRoomId);
    }
}
