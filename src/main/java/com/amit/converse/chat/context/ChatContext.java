package com.amit.converse.chat.context;

import com.amit.converse.chat.Interface.IChatRoom;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ChatContext<T extends IChatRoom> {
    protected T chatRoom;
}
