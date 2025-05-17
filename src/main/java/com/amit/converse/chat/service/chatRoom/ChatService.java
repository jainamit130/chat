package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.ChatRoomData;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.chatRoom.filfillmentService.factory.ChatRoomFulfilmentServiceFactory;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.MessageService.DeleteMessageService.ClearChatService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService<T extends ChatRoom> {
    @Autowired
    protected ChatMessageService chatMessageService;
    @Autowired
    protected IChatRoomRepository chatRoomRepository;
    @Autowired
    @Lazy
    private ClearChatService clearChatService;
    @Autowired
    private ChatRoomFulfilmentServiceFactory chatRoomFulfilmentServiceFactory;
    @Autowired
    private RedisReadService redisReadService;

    public T getContextChatRoom(String chatRoomId) {
        T chatRoom = (T) ChatContext.getChatRoom();
        if(chatRoom==null) {
            updateChatRoomContext((T) getChatRoomById(chatRoomId));
        }
        return getContextChatRoom();
    }

    public T getContextChatRoom() { return (T) ChatContext.getChatRoom(); }

    public void updateChatRoomContext(T chatRoom) {
        ChatContext.updateChatRoom(chatRoom);
    }

    public void clearContext() {
        ChatContext.clearContext();
    }

    public void readMessages(User user) {
        getContextChatRoom().readMessages(user.getUserId());
        processChatRoomToDB(getContextChatRoom());
    }

    public List<ChatRoom> getChatRoomsByIds(List<String> chatRoomIds,String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.getAllChatRoomsByIds(chatRoomIds,userId);
        chatRooms.stream().forEach(chatRoom -> {
            chatRoom.setChatRoomFulfilmentService(chatRoomFulfilmentServiceFactory.getFulfilmentService(chatRoom.getChatRoomType()));
            chatRoom.fulfill();
        });
        return chatRooms;
    }

    public ChatRoom getChatRoomById(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(chatRoomId));
        chatRoom.setChatRoomFulfilmentService(chatRoomFulfilmentServiceFactory.getFulfilmentService(chatRoom.getChatRoomType()));
        chatRoom.fulfill();
        return chatRoom;
    }

    protected T saveChat(T chat) {
        return chatRoomRepository.save(chat);
    }

    public void processChatRoomToDB(T chatRoom) {
        if (chatRoom.isDeletable()) {
            clearChatService.clearChat();
            chatRoomRepository.deleteById(chatRoom.getId());
            updateChatRoomContext(null);
        } else {
            updateChatRoomContext(saveChat(chatRoom));
        }
    }

    public List<String> getOnlineUserIdsOfChat() {
        List<String> onlineUserIds = new ArrayList<>(redisReadService.filterOnlineUsers(ChatContext.getChatRoom()));
        return onlineUserIds;
    }

    public void clearChat(String userId) {
        T chatRoom = (T) ChatContext.getChatRoom();
        chatRoom.clearChat(userId);
        processChatRoomToDB(chatRoom);
    }

    public List<ChatMessage> getMessagesOfChatRoom() {
        return chatMessageService.getMessagesToBeMarked(ChatContext.getChatRoom());
    }

    public void processSentMessage() {
        T chatRoom = getContextChatRoom();
        chatRoom.totalMessageCountIncrement();
        processChatRoomToDB(chatRoom);
    }

    public ChatRoomData getChatRoomData(String chatRoomId) {
        updateChatRoomContext((T) getChatRoomById(chatRoomId));
        ChatRoom chatRoom = getContextChatRoom();
        return ChatRoomData.builder().messages(getMessagesOfChatRoom()).
                onlineUsersDTO(chatRoom.transit())
                .build();
    }

}
