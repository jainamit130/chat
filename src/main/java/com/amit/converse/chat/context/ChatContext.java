package com.amit.converse.chat.context;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ChatContext {
    private IChatRoom chatRoom;
    private User user;
}
