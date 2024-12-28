package com.amit.converse.chat.model.ChatRooms;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chatRooms")
@EqualsAndHashCode(callSuper = true)
public class SelfChat extends ChatRoom {
}
