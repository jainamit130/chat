package com.amit.converse.chat.Interface;

import java.util.List;

public interface ITransactable extends IChatRoom {
    void join(List<String> userIds);
    void exit(List<String> userIds);
}
