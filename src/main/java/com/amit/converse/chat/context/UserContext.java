package com.amit.converse.chat.context;

import com.amit.converse.chat.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext {
    protected String userId;
    protected User user;
}
