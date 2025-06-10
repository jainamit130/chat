package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public abstract class UserChatNotification extends IUserNotification {
    @JsonProperty("chatRoom")
    protected IChatRoom chatRoom;

    protected TransactionNotification transactionNotification;

    public IChatRoom getChatRoom() {
        return chatRoom;
    }

    public TransactionNotification getTransactionNotification() {
        return transactionNotification;
    }

    public void populateOnlineUsersDTOInTransactionNotification(IOnlineUsersDTO onlineUsersDTO) {
        if(transactionNotification!=null) transactionNotification.setOnlineUsersDTO(onlineUsersDTO);
    }
}
