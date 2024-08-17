package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.AddMembersRequest;
import com.amit.converse.chat.dto.CreateGroupRequest;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.service.ChatService;
import com.amit.converse.chat.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final GroupService groupService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/sendMessage/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            chatService.addMessage(chatRoomId, chatMessage);
            messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
        } catch (IllegalArgumentException e) {
        }
    }

    @QueryMapping
    public List<ChatRoom> getChatRoomsOfUser(@Argument String userId) {
        List<ChatRoom> chatRooms=groupService.getChatRoomsOfUser(userId);
        return chatRooms;
    }

    @QueryMapping
    public List<ChatMessage> getMessagesOfChatRoom(@Argument String chatRoomId){
        List<ChatMessage> chatMessages=groupService.getMessagesOfChatRoom(chatRoomId);
        return chatMessages;
    }

//    @PostMapping("/test/grpc-call")
//    public ResponseEntity<String> testGrpcCall(@RequestBody Map<String, String> payload) {
//        String userId = payload.get("userId");
//        String chatWithUserId = payload.get("chatWithUserId");
//
//        // Call the gRPC method on userServiceClient
//        GetMessagesResponse response = userServiceClient.getMessages(userId, chatWithUserId);
//
//        // Handle response or return as needed
//        if (response != null && !response.getMessagesList().isEmpty()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("gRPC call executed successfully! Messages:\n");
//            for (com.amit.converse.common.ChatMessage message : response.getMessagesList()) {
//                sb.append("Message ID: ").append(message.getId()).append("\n");
//                sb.append("Sender ID: ").append(message.getSenderId()).append("\n");
//                sb.append("Receiver ID: ").append(message.getReceiverId()).append("\n");
//                sb.append("Content: ").append(message.getContent()).append("\n");
//                sb.append("Timestamp: ").append(message.getTimestamp()).append("\n");
//                sb.append("\n");
//            }
//            return ResponseEntity.ok(sb.toString());
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch messages from gRPC call");
//        }
//    }
}
