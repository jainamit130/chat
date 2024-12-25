package com.amit.converse.chat.State;

import com.amit.converse.chat.model.User;

public class Online extends State {

    public Online(User user) {
        super(user);
    }

    @Override
    public void transit() {
        user.setState(new Offline(user));
        user.transit();
    }
}
