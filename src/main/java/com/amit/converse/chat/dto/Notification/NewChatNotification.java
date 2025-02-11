package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.Interface.IChatRoom;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
@JsonDeserialize
public class NewChatNotification implements IUserNotification {
    @JsonProperty("chatRoom")
    private IChatRoom chatRoom;
}
