package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.Notification.ChatTransactionNotification;
import com.amit.converse.chat.dto.Notification.NotificationType;
import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.NotificationMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.NotificationMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private NotificationMessageService notificationMessageService;

    @Autowired
    private RedisReadService redisReadService;

    protected abstract String getTransactionMessage();

    protected abstract NotificationType getNotificationType();

    public final TransactionNotification generateMessage(ChatRoom chatRoom,User transactedUser, Instant notificationTime, List<String> userIdsToReceiveNotification,Boolean isChatHistoryShared) {
        String moderatorName = UserService.getUserContext().getDisplayName();
        String message = moderatorName + " " + getTransactionMessage() + " " + transactedUser.getDisplayName();
        NotificationMessage notificationMessage = notificationMessageService.fulfilMessage(NotificationMessageService.generateNotificationMessage(chatContext.getChatRoomId(), message, notificationTime,isChatHistoryShared));
        return TransactionNotification.builder().message(notificationMessageService.saveMessage(chatRoom,notificationMessage,userIdsToReceiveNotification)).chatRoomId(chatRoom.getId()).moderatorName(moderatorName).username(transactedUser.getDisplayName()).userId(transactedUser.getUserId()).onlineStatus(redisReadService.getConnectionStatus(transactedUser)).type(getNotificationType()).build();
    }

    public final void notifyGroup(ChatRoom chatRoom, List<TransactionNotification> transactionNotifications) {
        ChatTransactionNotification notification = ChatTransactionNotification.builder().notifications(transactionNotifications).build();
        chatNotificationService.sendNotification(chatRoom.getId(),notification);
    }
}
