package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.ExitedChatNotification;
import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Notification.NotifyGroupTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatDisconnectService extends ChatConnectionService {

    @Autowired
    public ChatDisconnectService(@Qualifier("exitNotification") NotifyGroupTransactionService notifyService) {
        this.notifyGroupTransactionService = notifyService;
    }

    protected void sendNotificationToUser(User user, ChatRoom exitedChatRoom, TransactionNotification transactionNotification) {
        userChatService.sendNotificationToUser(user,exitedChatRoom,new ExitedChatNotification(exitedChatRoom,transactionNotification));
    }

    protected void processChatConnection(User user, ChatRoom chatRoom) {
        chatRoom.disconnectChat(user.getUserId());
        user.disconnectChat(chatRoom.getId(),chatRoom.getUnreadMessageCount(user.getUserId()));
    }

}
