package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Notification.NotifyGroupTransactionService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class ChatConnectionService {
    @Autowired
    @Lazy
    protected UserChatService userChatService;

    protected NotifyGroupTransactionService notifyGroupTransactionService;

    private void processChatConnectionAndNotify(User user, ChatRoom chatRoom,TransactionNotification transactionNotification) {
        processChatConnection(user,chatRoom);
        sendNotificationToUser(user,chatRoom,transactionNotification);
    }

    protected abstract void sendNotificationToUser(User user, ChatRoom newChatRoom,TransactionNotification transactionNotification);

    protected abstract void processChatConnection(User user, ChatRoom chatRoom);

    protected abstract Instant getShiftedInstant();

    public abstract List<String> notificationReceiverIds(ChatRoom chatRoom, User transactedUser);

    public void processChatConnectionsAndNotify(List<User> users, ChatRoom chatRoom) {
        List<TransactionNotification> joinNotifications = new ArrayList<>();
        for(User user:users) {
            joinNotifications.add(notifyGroupTransactionService.generateMessage(chatRoom,user,getShiftedInstant(),notificationReceiverIds(chatRoom,user)));
            processChatConnectionAndNotify(user,chatRoom,joinNotifications.getLast());
        }
        notifyGroupTransactionService.notifyGroup(chatRoom,joinNotifications);
        userChatService.processUsersAndChatRoomToDB(users,chatRoom);
    }

    @Transactional
    public void processChatConnections(List<User> users, ChatRoom chatRoom) {
        for(User user:users) {
            processChatConnectionAndNotify(user,chatRoom,null);
        }
        userChatService.processUsersAndChatRoomToDB(users,chatRoom);
    }

    public void connectChatFromUserIds(List<String> userIds,ChatRoom chatRoom) {
        List<User> users = userChatService.getUsersFromRepo(userIds);
        processChatConnections(users,chatRoom);
    }
}
