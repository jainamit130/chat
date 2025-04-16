package com.amit.converse.chat.context.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.IContext;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class ChatContext {

    private static final ThreadLocal<IChatRoom> chatContextHolder = new ThreadLocal<>();

    public static void setChatRoom(IChatRoom chatRoom) {
        chatContextHolder.set(chatRoom);
        if (chatRoom != null) chatRoom.transit();
    }

    public static void updateChatRoom(IChatRoom chatRoom) {
        chatContextHolder.set(chatRoom);
    }

    public static IChatRoom getChatRoom() {
        return chatContextHolder.get();
    }

    public static String getChatRoomId() {
        IChatRoom chatRoom = chatContextHolder.get();
        return chatRoom != null ? chatRoom.getId() : null;
    }

    public static void clearContext() {
        chatContextHolder.remove();
    }
}

