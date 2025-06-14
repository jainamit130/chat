package com.amit.converse.chat.service.User;

import com.amit.converse.chat.State.StateFactoryService;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsService {

    @Autowired
    @Lazy
    private DirectChatUserService directChatUserService;

    @Autowired
    @Lazy
    private GroupChatUserService groupChatUserService;

    @Autowired
    @Lazy
    private StateFactoryService stateFactoryService;

    @Autowired
    @Lazy
    private SelfChatUserService selfChatUserService;

    private UserDetails getInitializedUserDetails(User user) {
        user.setState(stateFactoryService.getState(user));
        UserDetails userDetails = UserDetails.builder()
                .userId(user.getUserId())
                .username(user.getDisplayName())
                .userStatus(user.getStatus())
                .status(user.getConnectionStatus())
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
