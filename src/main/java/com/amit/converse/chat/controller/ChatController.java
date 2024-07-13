package com.amit.converse.chat.controller;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private TestService testService;

    @MessageMapping("/chat.sendMessage/{chatRoomId}")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@DestinationVariable String chatRoomId, @Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }

    @GetMapping("/chat/messages/{chatRoomId}")
    public List<ChatMessage> getMessages(@PathVariable String chatRoomId) {
        return testService.getMessages(chatRoomId);
    }

    @PostMapping("/chat/sendMessage/{chatRoomId}")
    public ChatMessage postMessage(@PathVariable String chatRoomId, @RequestBody ChatMessage chatMessage) {
        testService.addMessage(chatRoomId, chatMessage);
        return chatMessage;
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
