package com.amit.converse.chat.service.User;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsService {

    @Autowired
    private DirectChatUserService directChatUserService;

    @Autowired
    private GroupChatUserService groupChatUserService;

    @Autowired
    private SelfChatUserService selfChatUserService;

    private UserDetails getInitializedUserDetails(User user) {
        UserDetails userDetails = UserDetails.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .userStatus(user.getStatus())
                .commonGroupChatIds(new ArrayList<>())
                .lastSeenTimestamp(user.getLastSeenTimestamp())
                .build();
        return userDetails;
    }

    public UserDetails getProfileDetails(User user) {
        UserDetails userDetails = getInitializedUserDetails(user);
        userDetails.setCommonChatId(selfChatUserService.getSelfChat().getId());
        return userDetails;
    }

    public UserDetails getUserDetails(User user) {
        UserDetails userDetails = getInitializedUserDetails(user);
        userDetails.setCommonChatId(directChatUserService.getCommonChatId(user.getUserId()));
        userDetails.setCommonGroupChatIds(groupChatUserService.getCommonChatIds(userDetails.getUserId()));
        return userDetails;
    }
}
