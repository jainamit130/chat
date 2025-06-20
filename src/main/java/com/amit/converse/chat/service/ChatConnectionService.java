package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
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

    protected abstract Instant getShiftedInstant(GroupChat chatRoom, User user);

    public abstract List<String> notificationReceiverIds(ChatRoom chatRoom, User transactedUser);

    public void processChatConnectionsAndNotify(List<User> users, GroupChat chatRoom) {
        List<TransactionNotification> transactionNotifications = new ArrayList<>();
        for(User user:users) {
            transactionNotifications.add(notifyGroupTransactionService.generateMessage(chatRoom,user,getShiftedInstant(chatRoom,user),notificationReceiverIds(chatRoom,user)));
            processChatConnectionAndNotify(user,chatRoom,transactionNotifications.getLast());
        }
        notifyGroupTransactionService.notifyGroup(chatRoom,transactionNotifications);
        userChatService.processUsersAndChatRoomToDB(users,chatRoom);
    }

    public void processChatConnections(List<User> users, ChatRoom chatRoom) {
        for(User user:users) {
            processChatConnectionAndNotify(user,chatRoom,null);
        }
    }

    public void connectChatFromUsers(List<User> users,ChatRoom chatRoom) {
        processChatConnections(users,chatRoom);
    }
}
