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

    protected abstract Instant getShiftedInstant(GroupChat chatRoom, User user, List<Message> messageHistory);

    protected abstract void processChatConnectionAndNotify(User user,ChatRoom chatRoom,List<Message> messagesHistory,TransactionNotification notification);

    public abstract List<String> notificationReceiverIds(ChatRoom chatRoom, User transactedUser);

    public void processChatConnectionsAndNotify(List<User> users, GroupChat chatRoom,Map<String,List<Message>> messagesHistoryMap, Boolean isChatHistoryShared) {
        List<TransactionNotification> transactionNotifications = new ArrayList<>();
        for(User user:users) {
            List<Message> messagesHistory = messagesHistoryMap.getOrDefault(user.getUserId(), new ArrayList<>());
            transactionNotifications.add(notifyGroupTransactionService.generateMessage(chatRoom,user,getShiftedInstant(chatRoom,user,messagesHistory),notificationReceiverIds(chatRoom,user),isChatHistoryShared));
            processChatConnectionAndNotify(user,chatRoom, messagesHistory,transactionNotifications.getLast());
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
