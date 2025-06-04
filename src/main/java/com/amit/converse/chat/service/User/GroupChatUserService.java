package com.amit.converse.chat.service.User;

import com.amit.converse.chat.dto.GroupDetails;
import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.GroupChatService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupChatUserService extends UserChatService<GroupChat> {

    @Autowired
    private GroupChatService groupChatService;

    public void exit(List<User> users) {
        disconnectChat(users,chatService.getContextChatRoom());
        processUsersToDB(users);
    }

    public GroupDetails getGroupDetails() {
        GroupChat chatRoom = chatService.getContextChatRoom();
        List<User> users = getUsersFromRepo(chatRoom.getUserIds());
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user: users) {
            UserDTO userDTO = UserDTO.builder().userId(user.getUserId()).username(user.getDisplayName()).build();
            userDTOList.add(userDTO);
        }
        return GroupDetails.builder().members(userDTOList).build();
    }

    public List<String> getCommonChatIds(String userId) {
        return groupChatService.getCommonChats(UserService.getUserContext().getUserId(),userId).stream().map(groupChat -> groupChat.getId()).collect(Collectors.toList());
    }
}
