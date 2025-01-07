package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.dto.Notification.ChatTransactionNotification;
import com.amit.converse.chat.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NotifyGroupExitService {
    private final ChatContext<ITransactable> chatContext;
    private final ChatNotificationService chatNotificationService;

    private String getMessage(User addedByUser, User joinedUser) {
        return addedByUser.getUsername() + " removed by " + joinedUser.getUsername();
    }

    public void notifyGroup(List<User> joinedUsers) {
        User addedByUser = chatContext.getUser();
        ITransactable chatRoom = chatContext.getChatRoom();
        List<String> joinNotifications = new ArrayList<>();
        for(User joinedUser : joinedUsers) {
            joinNotifications.add(getMessage(addedByUser,joinedUser));
        }
        ChatTransactionNotification notification = ChatTransactionNotification.builder().notifications(joinNotifications).build();
        chatNotificationService.sendNotification(chatRoom.getId(),notification);
    }
}
