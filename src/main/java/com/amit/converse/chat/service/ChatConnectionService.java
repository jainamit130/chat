package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.dto.Notification.UserChatNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Notification.NotifyGroupTransactionService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ChatConnectionService {
    @Autowired
    @Lazy
    protected UserChatService userChatService;

    @Autowired
    @Lazy
    protected ChatMessageService chatMessageService;

    protected NotifyGroupTransactionService notifyGroupTransactionService;

    protected abstract Instant getShiftedInstant(GroupChat chatRoom, User user);

    protected abstract void processChatConnectionAndNotify(User user,ChatRoom chatRoom,List<Message> messagesHistory,TransactionNotification notification);

    public abstract List<String> notificationReceiverIds(ChatRoom chatRoom, User transactedUser);

    public void processChatConnectionsAndNotify(List<User> users, GroupChat chatRoom) {
        List<TransactionNotification> transactionNotifications = new ArrayList<>();
        Map<String,List<Message>> messagesHistoryMap = chatMessageService.processMemberCountOfMessages(chatRoom,users);
        for(User user:users) {
            transactionNotifications.add(notifyGroupTransactionService.generateMessage(chatRoom,user,getShiftedInstant(chatRoom,user),notificationReceiverIds(chatRoom,user)));
            processChatConnectionAndNotify(user,chatRoom,messagesHistoryMap.get(user.getUserId()),transactionNotifications.getLast());
        }
        notifyGroupTransactionService.notifyGroup(chatRoom,transactionNotifications);
        userChatService.processUsersAndChatRoomToDB(users,chatRoom);
    }

    public void processChatConnections(List<User> users, ChatRoom chatRoom) {
        for(User user:users) {
            processChatConnectionAndNotify(user,chatRoom,new ArrayList<>(),null);
        }
    }

    public void connectChatFromUsers(List<User> users,ChatRoom chatRoom) {
        processChatConnections(users,chatRoom);
    }
}
