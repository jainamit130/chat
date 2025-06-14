package com.amit.converse.chat.context.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.stereotype.Component;

@Component
public class ChatContext {

    private static ThreadLocal<IChatRoom> chatContext = new ThreadLocal<>();

    public static void setChatRoom(IChatRoom chatRoom) {
        updateChatRoom(chatRoom);
        if (chatRoom != null) chatRoom.transit();
    }

    public static void updateChatRoom(IChatRoom chatRoom) {
        chatContext.set(chatRoom);
    }

    public static IChatRoom getChatRoom() {
        return chatContext.get();
    }

    public static String getChatRoomId() {
        IChatRoom chatRoom = chatContext.get();
        return chatRoom != null ? chatRoom.getId() : null;
    }

    public static void clearContext() {
        chatContext.remove();
    }
}

