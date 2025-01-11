package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupChatUserService extends UserService<ITransactable> {

    public void joinChat(List<String> userIds) {
        for(String userId : userIds) {
            joinChat(userId);
        }
    }

    public void exitChatRoom() {
        User user = userContext.getUser();
        IChatRoom chatRoom = chatContext.getChatRoom();
        user.exitChatRoom(chatRoom.getId());
        processUserToDB(user);
    }
}
