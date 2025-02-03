package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService<T extends IChatRoom> {
    @Autowired
    protected ChatContext<T> context;
    @Autowired
    protected ChatMessageService chatMessageService;
    @Autowired
    protected IChatRoomRepository chatRoomRepository;
    @Autowired
    private RedisReadService redisReadService;

    public void updateChatRoomContext(T chatRoom) {
        context.setChatRoom(chatRoom);
    }

    public void sendMessage(ChatMessage message) throws InterruptedException {
        chatMessageService.sendMessage(message);
    }

    public List<IChatRoom> getChatRoomsByIds(List<String> chatRoomIds) {
        return chatRoomRepository.findAllById(chatRoomIds);
    }

    public IChatRoom getChatRoomById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(chatRoomId));
    }

    protected T saveChat(T chat) {
        return chatRoomRepository.save(chat);
    }

    public void processChatRoomToDB(T chatRoom) {
        if (chatRoom.isDeletable()) {
            chatRoomRepository.deleteById(chatRoom.getId());
            updateChatRoomContext(null);
        } else {
            updateChatRoomContext(saveChat(chatRoom));
        }
    }

    public void deleteChat() {
        processChatRoomToDB(context.getChatRoom());
    }

    public List<String> getOnlineUserIdsOfChat() {
        List<String> onlineUserIds = new ArrayList<>(redisReadService.filterOnlineUsers(context.getChatRoom().getUserIds()));
        return onlineUserIds;
    }

    public void clearChat(String userId) {
        T chatRoom = context.getChatRoom();
        chatRoom.clearChat(userId);
        processChatRoomToDB(chatRoom);
    }

//    clearChat(ChatRoom,UserId) => Clear Chat uses chatRoom field updates it and saves it, it does not notify
//    deleteChat(ChatRoom,User)
//    getOnlineUsersOfChat(ChatRoom) UserService Needed
//    getMessagesOfChatRoom(ChatRoom,UserId) UserService Needed
//    createChat(List<> memberIds, chatCreationMetadata) UserService Needed
//    addMembers
//    removeMembers
//    sendNotificationMessage
//
}
