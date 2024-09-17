package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.MessageStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class MarkMessageService {

    private final WebSocketMessageService webSocketMessageService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final GroupService groupService;

    public void markAllMessagesDelivered(String userId){
        User user = userService.getUser(userId);
        if (user != null) {
            Set<String> chatRoomIds = user.getChatRoomIds();
            if (chatRoomIds != null && !chatRoomIds.isEmpty()) {
                for(String chatRoomId: chatRoomIds){
                    ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
                    Integer toBeDeliveredMessagesCount=chatRoom.getUndeliveredMessageCount(userId);
                    if(toBeDeliveredMessagesCount>0)
                        markAllMessages(chatRoom, userId, true,toBeDeliveredMessagesCount);
                }
            }
        } else {
            System.out.println("User not found: " + userId);
        }
    }


    public void markAllMessagesRead(String chatRoomId,String userId){
        ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
        Integer toBeMarkedMessagesCount=chatRoom.getUnreadMessageCount(userId);
        if(toBeMarkedMessagesCount>0)
            markAllMessages(chatRoom,userId,false,toBeMarkedMessagesCount);
    }

    public void markAllMessages(ChatRoom chatRoom,String userId, Boolean isDelivered, Integer toBeMarkedMessagesCount) {
        PageRequest pageRequest = PageRequest.of(0, toBeMarkedMessagesCount, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ChatMessage> messagesToBeMarked = groupService.getMessagesToBeMarked(chatRoom.getId(),pageRequest);
        String timestampStr = Instant.now().toString();
        for (ChatMessage messageToBeMarked: messagesToBeMarked){
            if(isDelivered){
                messageToBeMarked.addUserToDeliveredReceipt(timestampStr,userId);
                if(messageToBeMarked.getDeliveryReceiptsByTime().size()==chatRoom.getUserIds().size()){
                    messageToBeMarked.setMessageStatus(MessageStatus.DELIVERED);
                    webSocketMessageService.sendMarkedMessageStatus(chatRoom.getId(),messageToBeMarked.getSenderId(),false);
                }
            } else {
                messageToBeMarked.addUserToReadReceipt(timestampStr,userId);
                if(messageToBeMarked.getReadReceiptsByTime().size()==chatRoom.getUserIds().size()){
                    messageToBeMarked.setMessageStatus(MessageStatus.READ);
                    webSocketMessageService.sendMarkedMessageStatus(chatRoom.getId(),messageToBeMarked.getSenderId(),false);
                }
            }
            chatMessageRepository.save(messageToBeMarked);
        }
        if(!isDelivered) {
            chatRoom.allMessagesMarkedRead(userId);
        } else {
            chatRoom.allMessagesMarkedDelivered(userId);
        }
        groupService.saveChatRoom(chatRoom);
        return;
    }
}
