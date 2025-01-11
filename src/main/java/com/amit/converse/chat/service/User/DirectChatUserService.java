package com.amit.converse.chat.service.User;

import org.springframework.stereotype.Service;

@Service
public class DirectChatUserService extends UserService {

    public void startChat(String counterPartUserId) {
        String userId = userContext.getUser().getUserId();
        joinChat(userId);
        joinChat(counterPartUserId);
    }
}
