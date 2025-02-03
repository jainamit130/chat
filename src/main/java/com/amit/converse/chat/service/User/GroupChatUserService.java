package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.dto.GroupDetails;
import com.amit.converse.chat.dto.OnlineUsers.GroupChatOnlineUsersDto;
import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.GroupChatService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupChatUserService extends UserChatService<ITransactable> {

    @Autowired
    private GroupChatService groupChatService;

    public void processCreation() {
        ITransactable chatRoom = chatContext.getChatRoom();
        List<User> users = getUsersFromRepo(chatRoom.getUserIds());
        for(User user : users) {
            connectChat(user);
        }
        processUsersToDB(users);
    }

    public void join(List<User> users) {
        IChatRoom chatRoom = chatContext.getChatRoom();
        for(User user:users) {
            user.connectChat(chatRoom.getId());
        }
        processUsersToDB(users);
    }

    public void exit(List<User> users) {
        IChatRoom chatRoom = chatContext.getChatRoom();
        for(User user:users) {
            user.disconnectChat(chatRoom.getId(),chatRoom.getUnreadMessageCount(user.getUserId()));
        }
        processUsersToDB(users);
    }

    public GroupChatOnlineUsersDto getOnlineUsersDTO(List<String> onlineUserIds) {
        List<User> onlineUsers = getUsersFromRepo(onlineUserIds);
        return GroupChatOnlineUsersDto.builder().onlineUsers(processUsersToUsernames(onlineUsers)).build();
    }

    public GroupDetails getGroupDetails() {
        IChatRoom chatRoom = chatContext.getChatRoom();
        List<User> users = getUsersFromRepo(chatRoom.getUserIds());
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user: users) {
            UserDTO userDTO = UserDTO.builder().userId(user.getUserId()).username(user.getUsername()).build();
            userDTOList.add(userDTO);
        }
        return GroupDetails.builder().members(userDTOList).build();
    }

    public List<String> getCommonChatIds(String userId) {
        return groupChatService.getCommonChats(userContext.getUserId(),userId).stream().map(groupChat -> groupChat.getId()).collect(Collectors.toList());
    }
}
