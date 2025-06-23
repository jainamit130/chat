package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.ExitedChatNotification;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Notification.NotifyGroupTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatDisconnectService extends ChatConnectionService {

    @Autowired
    public ChatDisconnectService(@Qualifier("exitNotification") NotifyGroupTransactionService notifyService) {
        this.notifyGroupTransactionService = notifyService;
    }

    @Override
    protected void processChatConnectionAndNotify(User user, ChatRoom chatRoom, List<Message> messagesHistory, TransactionNotification notification) {
        processChatConnection(user,chatRoom);
        userChatService.sendNotificationToUser(user,chatRoom,new ExitedChatNotification(chatRoom,notification,messagesHistory));
    }

    protected void processChatConnection(User user, ChatRoom chatRoom) {
        user.disconnectChat(chatRoom.getId(),chatRoom.getUnreadMessageCount(user.getUserId()));
        chatRoom.fulfill(user);
        chatRoom.disconnectChat(user.getUserId());
    }

    @Override
    protected Instant getShiftedInstant(GroupChat chatRoom, User user, List<Message> messageHistory) {
        return chatRoom.getLastAvailableInstant(user.getUserId()).minusMillis(1);
    }

    @Override
    public List<String> notificationReceiverIds(ChatRoom chatRoom, User transactedUser) {
        // for exit service the user is not yet disconnected
        return chatRoom.getUserIds();
    }

}
