package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.Notification.ChatTransactionNotification;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.NotificationMessageService;
import com.amit.converse.chat.service.MessageService.SaveNotificationMessageService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public abstract class NotifyGroupTransactionService {

    @Autowired
    private ChatContext chatContext;

    @Autowired
    private ChatNotificationService chatNotificationService;

    @Autowired
    private SaveNotificationMessageService saveNotificationMessageService;

    protected abstract String getTransactionMessage();

    protected final String generateMessage(User joinedUser) {
        String message = UserService.getUserContext().getDisplayName() + " " + getTransactionMessage() + " " + joinedUser.getDisplayName();
        saveNotificationMessageService.saveMessage(NotificationMessageService.generateNotificationMessage(chatContext.getChatRoomId(),message));
        return message;
    }

    public final void notifyGroup(List<User> joinedUsers) {
        ITransactable chatRoom = (ITransactable)chatContext.getChatRoom();
        List<String> joinNotifications = new ArrayList<>();
        for(User joinedUser : joinedUsers) {
            joinNotifications.add(generateMessage(joinedUser));
        }
        ChatTransactionNotification notification = ChatTransactionNotification.builder().notifications(joinNotifications).build();
        chatNotificationService.sendNotification(chatRoom.getId(),notification);
    }
}
