package com.amit.converse.chat.context.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatContext {

    private static IChatRoom chatContext;

    public static void setChatRoom(IChatRoom chatRoom) {
        updateChatRoom(chatRoom);
        if (chatRoom != null) chatRoom.transit();
    }

    public static void updateChatRoom(IChatRoom chatRoom) {
        chatContext=chatRoom;
    }

    public static IChatRoom getChatRoom() {
        return chatContext;
    }

    public static String getChatRoomId() {
        IChatRoom chatRoom = chatContext;
        return chatRoom != null ? chatRoom.getId() : null;
    }

    public static void clearContext() {
        chatContext=null;
    }
}

