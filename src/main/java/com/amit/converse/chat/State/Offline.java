package com.amit.converse.chat.State;

import com.amit.converse.chat.model.User;

public class Offline extends State {
    public Offline(User user) {
        super(user);
    }

    @Override
    public void transit() {
        user.setState(new Online(user));
        user.transit();
    }
}
