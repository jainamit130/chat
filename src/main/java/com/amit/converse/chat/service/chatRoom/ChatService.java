package com.amit.converse.chat.service.chatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.ChatRoomData;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.User.UserService;
import com.amit.converse.chat.service.chatRoom.MessageFilters.MessageFilterFactory;
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
    @Lazy
    private UserChatService userChatService;
    @Autowired
    private ChatRoomFulfilmentServiceFactory chatRoomFulfilmentServiceFactory;
    @Autowired
    private RedisReadService redisReadService;
    @Autowired
    private MessageFilterFactory messageFilterFactory;

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

    public static void clearContext() {
        ChatContext.clearContext();
    }

    public void readMessages(ChatRoom chatRoom,User user) {
        chatRoom.readMessages(user.getUserId());
        processChatRoomToDB((T) chatRoom);
    }

    public List<ChatRoom> getChatRoomsByIds(List<String> chatRoomIds, User user) {
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomRepository.getAllChatRoomsByIds(chatRoomIds, user.getUserId()));
        chatRooms.forEach((chatRoom) -> {
            fulfillChatRoom(chatRoom, user);
        });
        return chatRooms;
    }

    public ChatRoom getChatRoomById(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(chatRoomId));
        return fulfillChatRoom(chatRoom,UserService.getUserContext());
    }

    public ChatRoom fulfillChatRoom(ChatRoom chatRoom, User user) {
        chatRoom.setChatRoomFulfilmentService(chatRoomFulfilmentServiceFactory.getFulfilmentService(chatRoom.getChatRoomType()));
        chatRoom.fulfill(user);
        return chatRoom;
    }

    private void deleteChatFromDB(ChatRoom chatRoom) {
        clearChatService.clearChat(chatRoom);
        userChatService.deleteChat(chatRoom);
        chatRoomRepository.deleteById(chatRoom.getId());
    }

    protected T saveChat(T chat) {
        return chatRoomRepository.save(chat);
    }

    public void processChatRoomToDB(T chatRoom) {
        if (chatRoom.isDeletable()) {
            deleteChatFromDB(chatRoom);
            updateChatRoomContext(null);
        } else {
            updateChatRoomContext((T) fulfillChatRoom(saveChat(chatRoom),UserService.getUserContext()));
        }
    }

    public List<String> getOnlineUserIdsOfChat(IChatRoom chatRoom) {
        List<String> onlineUserIds = new ArrayList<>(redisReadService.filterOnlineUsers(chatRoom));
        return onlineUserIds;
    }

    public void clearChat(IChatRoom chatRoom,String userId) {
        chatRoom.clearChat(userId);
    }

    public List<Message> getMessagesOfChatRoom() {
        return messageFilterFactory.getBlindPeriodFilter(ChatContext.getChatRoom().getChatRoomType()).filterBlindSpots((ChatRoom) ChatContext.getChatRoom(), UserService.getUserContext(),chatMessageService.getMessagesOfChatRoom(ChatContext.getChatRoom()));
    }

    public void processSentMessage(ChatRoom chatRoom) {
        chatRoom.totalMessageCountIncrement();
    }

    public IOnlineUsersDTO transit(ChatRoom chatRoom) {
        return chatRoom.transit();
    }

    public ChatRoomData getChatRoomData(String chatRoomId) {
        updateChatRoomContext((T) getChatRoomById(chatRoomId));
        ChatRoom chatRoom = getContextChatRoom();
        return ChatRoomData.builder().messages(getMessagesOfChatRoom()).
                onlineUsersDTO(transit(chatRoom))
                .build();
    }

}
