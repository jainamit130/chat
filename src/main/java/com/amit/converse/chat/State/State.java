package com.amit.converse.chat.State;

import com.amit.converse.chat.Redis.Transition;
import com.amit.converse.chat.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public abstract class State implements Transition {
    protected final User user;
}
