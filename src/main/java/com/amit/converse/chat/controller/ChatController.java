package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.MessageInfoDto;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.service.ChatService;
import com.amit.converse.chat.service.GroupService;
import com.amit.converse.chat.service.MarkMessageService;
import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    private final ChatService chatService;
    private final GroupService groupService;
    private final MarkMessageService markMessageService;
    private final WebSocketMessageService webSocketMessageService;

    @GetMapping("/chat/message/info/{chatMessageId}")
    public ResponseEntity<MessageInfoDto> getMessageInfo(@PathVariable String chatMessageId) {
        return new ResponseEntity<MessageInfoDto>(chatService.getMessageInfo(chatMessageId), HttpStatus.OK);
    }

    @MessageMapping("/chat/sendMessage/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, ChatMessage chatMessage) throws InterruptedException {
        ChatMessage savedMessage = chatService.addMessage(chatRoomId, chatMessage, false);
        webSocketMessageService.sendMessage(chatRoomId,savedMessage);
    }

    @MutationMapping
    public Boolean markAllMessagesRead(@Argument String chatRoomId, @Argument String userId) {
        markMessageService.markAllMessagesRead(chatRoomId, userId);
        return true;
    }

    @MutationMapping
    public Boolean markAllMessagesDelivered(@Argument String userId) {
        markMessageService.markAllMessagesDelivered(userId);
        return true;
    }

    @QueryMapping
    public List<ChatRoom> getChatRoomsOfUser(@Argument String userId) {
        List<ChatRoom> chatRooms=groupService.getChatRoomsOfUser(userId);
        return chatRooms;
    }

    @QueryMapping
    public List<ChatMessage> getMessagesOfChatRoom(@Argument String chatRoomId, @Argument String userId ,@Argument Integer fromCount){
        List<ChatMessage> chatMessages=groupService.getMessagesOfChatRoom(chatRoomId,userId,fromCount);
        return chatMessages;
    }

    @PostMapping("/chat/message/deleteForMe/{messageId}")
    public ResponseEntity<Boolean> deleteForMe(@PathVariable String messageId, @RequestBody String userId){
        return new ResponseEntity(chatService.deleteForMe(messageId,userId),HttpStatus.OK);
    }

    @PostMapping("/chat/message/deleteForEveryone/{messageId}")
    public ResponseEntity<Boolean> deleteMessageForEveryone(@PathVariable String messageId, @RequestBody String userId){
        return new ResponseEntity(chatService.deleteForEveryone(messageId,userId),HttpStatus.OK);
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
