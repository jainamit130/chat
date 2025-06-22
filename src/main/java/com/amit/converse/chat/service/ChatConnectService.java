package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.TransactionNotification;
import com.amit.converse.chat.dto.Notification.UserChatNotification;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Notification.NotifyGroupTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatConnectService extends ChatConnectionService {

    @Autowired
    public ChatConnectService(@Qualifier("joinNotification") NotifyGroupTransactionService notifyService) {
        this.notifyGroupTransactionService = notifyService;
    }

    protected void processChatConnection(User user, ChatRoom chatRoom) {
        if(user.isExited(chatRoom.getId())) chatRoom.updateReadMessageCountOfExitedUser(user.getUserId(),user.getUnreadMessageCountOfExitedChat(chatRoom.getId()));
        user.connectChat(chatRoom.getId());
        chatRoom.fulfill(user);
        chatRoom.connectChat(user.getUserId());
    }

    @Override
    protected Instant getShiftedInstant(GroupChat chatRoom, User user) {
        return chatRoom.getLastAvailableInstant(user.getUserId()).plusMillis(1);
    }

    @Override
    protected void processChatConnectionAndNotify(User user, ChatRoom chatRoom, List<Message> messagesHistory, TransactionNotification notification) {
        processChatConnection(user,chatRoom);
        userChatService.sendNotificationToUser(user,chatRoom,new NewChatNotification(chatRoom,notification,messagesHistory));
    }

    @Override
    public List<String> notificationReceiverIds(ChatRoom chatRoom, User transactedUser) {
        List<String> userIds = new ArrayList<>(chatRoom.getUserIds());
        userIds.add(transactedUser.getUserId());
        return userIds;
    }

}
