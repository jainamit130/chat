package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Notification.NotifyGroupTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatConnectService extends ChatConnectionService {

    @Autowired
    public ChatConnectService(@Qualifier("joinNotification") NotifyGroupTransactionService notifyService) {
        this.notifyGroupTransactionService = notifyService;
    }

    protected void sendNotificationToUser(User user, ChatRoom newChatRoom, TransactionNotification transactionNotification) {
        userChatService.sendNotificationToUser(user,newChatRoom,new NewChatNotification(newChatRoom,transactionNotification));
    }

    protected void processChatConnection(User user, ChatRoom chatRoom) {
        chatRoom.fulfill(user);
        chatRoom.connectChat(user.getUserId());
        user.connectChat(chatRoom.getId());
    }

}
