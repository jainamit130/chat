package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupChatUserService extends UserChatService<ITransactable> {

    public void processCreation() {
        ITransactable chatRoom = chatContext.getChatRoom();
        List<User> users = getUsersFromRepo(chatRoom.getUserIds());
        for(User user : users) {
            connectChat(user);
        }
        processUsersToDB(users);
    }


    public void join(List<String> userIds) {
        List<User> users = getUsersFromRepo(userIds);
        IChatRoom chatRoom = chatContext.getChatRoom();
        for(User user:users) {
            user.connectChat(chatRoom.getId());
        }
        processUsersToDB(users);
    }

    public void exit(List<String> userIds) {
        List<User> users = getUsersFromRepo(userIds);
        IChatRoom chatRoom = chatContext.getChatRoom();
        for(User user:users) {
            user.disconnectChat(chatRoom.getId());
        }
        processUsersToDB(users);
    }
}
