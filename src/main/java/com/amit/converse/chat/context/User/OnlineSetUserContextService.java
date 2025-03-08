package com.amit.converse.chat.context.User;

import com.amit.converse.chat.State.State;
import com.amit.converse.chat.model.User;
import org.springframework.stereotype.Service;

@Service
public class OnlineSetUserContextService extends SerUserContextService {
    @Override
    public State getState(User user) {
        return stateFactoryService.getOnlineState(user);
    }
}
