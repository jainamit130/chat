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
public abstract class NotifyGroupTransactionService {

    protected final ChatContext<ITransactable> chatContext;
    protected final ChatNotificationService chatNotificationService;

    protected abstract String getTransactionMessage();

    protected final String getMessage(User joinedUser) {
        return chatContext.getUser().getUsername() + " " + getTransactionMessage() + " " + joinedUser.getUsername();
    }

    public final void notifyGroup(List<User> joinedUsers) {
        ITransactable chatRoom = chatContext.getChatRoom();
        List<String> joinNotifications = new ArrayList<>();
        for(User joinedUser : joinedUsers) {
            joinNotifications.add(getMessage(joinedUser));
        }
        ChatTransactionNotification notification = ChatTransactionNotification.builder().notifications(joinNotifications).build();
        chatNotificationService.sendNotification(chatRoom.getId(),notification);
    }
}
