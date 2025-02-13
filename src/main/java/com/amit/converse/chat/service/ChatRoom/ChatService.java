package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService<T extends ChatRoom> {
    @Autowired
    protected ChatContext<T> context;
    @Autowired
    protected ChatMessageService chatMessageService;
    @Autowired
    protected IChatRoomRepository chatRoomRepository;
    @Autowired
    private RedisReadService redisReadService;

    public T getContextChatRoom() { return context.getChatRoom(); }

    public void updateChatRoomContext(T chatRoom) {
        context.setChatRoom(chatRoom);
    }

    public void sendMessage(ChatMessage message) throws InterruptedException {
        chatMessageService.sendMessage(message);
    }

    public void readMessages(User user) {
        getContextChatRoom().readMessages(user.getUserId());
        processChatRoomToDB(getContextChatRoom());
    }

    public List<ChatRoom> getChatRoomsByIds(List<String> chatRoomIds,String userId) {
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
        List<String> onlineUserIds = new ArrayList<>(redisReadService.filterOnlineUsers(context.getChatRoom()));
        return onlineUserIds;
    }

    public void clearChat(String userId) {
        T chatRoom = context.getChatRoom();
        chatRoom.clearChat(userId);
        processChatRoomToDB(chatRoom);
    }

    public List<ChatMessage> getMessagesOfChatRoom() {
        return chatMessageService.getMessagesOfChatFrom(context.getChatRoom());
    }

}
