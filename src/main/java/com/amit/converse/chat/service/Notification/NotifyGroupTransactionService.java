package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.Notification.ChatTransactionNotification;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.MessageService.NotificationMessageService;
import com.amit.converse.chat.service.MessageService.SaveMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public abstract class NotifyGroupTransactionService {

    @Autowired
    protected ChatContext<ITransactable> chatContext;

    @Autowired
    protected UserContext userContext;

    @Autowired
    protected ChatNotificationService chatNotificationService;

    @Autowired
    protected SaveMessageService saveMessageService;

    protected abstract String getTransactionMessage();

    protected final String generateMessage(User joinedUser) {
        String message = userContext.getUser().getUsername() + " " + getTransactionMessage() + " " + joinedUser.getUsername();
        saveMessageService.saveMessage(NotificationMessageService.generateNotificationMessage(chatContext.getChatRoomId(),message));
        return message;
    }

    public final void notifyGroup(List<User> joinedUsers) {
        ITransactable chatRoom = chatContext.getChatRoom();
        List<String> joinNotifications = new ArrayList<>();
        for(User joinedUser : joinedUsers) {
            joinNotifications.add(generateMessage(joinedUser));
        }
        ChatTransactionNotification notification = ChatTransactionNotification.builder().notifications(joinNotifications).build();
        chatNotificationService.sendNotification(chatRoom.getId(),notification);
    }
}
