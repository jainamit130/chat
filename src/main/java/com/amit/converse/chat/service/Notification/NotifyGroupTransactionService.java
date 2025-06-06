package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.Notification.ChatTransactionNotification;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.NotificationMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.NotificationMessageService;
import com.amit.converse.chat.service.MessageService.SaveNotificationMessageService;
import com.amit.converse.chat.service.User.UserChatService;
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
    private UserNotificationService userNotificationService;

    @Autowired
    private SaveNotificationMessageService saveNotificationMessageService;

    protected abstract String getTransactionMessage();

    public final TransactionNotification generateMessage(User joinedUser) {
        String moderatorName = UserService.getUserContext().getDisplayName();
        String message = moderatorName + " " + getTransactionMessage() + " " + joinedUser.getDisplayName();
        NotificationMessage notificationMessage = NotificationMessageService.generateNotificationMessage(chatContext.getChatRoomId(), message);
        saveNotificationMessageService.saveMessage(notificationMessage);
        return TransactionNotification.builder().message(notificationMessage).moderatorName(moderatorName).username(joinedUser.getDisplayName()).build();
    }

    public final void notifyGroup(ChatRoom chatRoom, List<TransactionNotification> transactionNotifications) {
        ChatTransactionNotification notification = ChatTransactionNotification.builder().notifications(transactionNotifications).build();
        chatNotificationService.sendNotification(chatRoom.getId(),notification);
    }
}
