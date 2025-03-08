package com.amit.converse.chat.context.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.IContext;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ChatContext<T extends IChatRoom> implements IContext {
    protected T chatRoom;

    public String getChatRoomId() {
        return chatRoom.getId();
    }

    @Override
    public void clearContext() {
        this.setChatRoom(null);
    }
}
