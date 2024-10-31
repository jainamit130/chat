package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.MessageStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

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

    @Transactional
    public void markAllMessages(ChatRoom chatRoom,String userId, Boolean isDelivered, Integer toBeMarkedMessagesCount) {
        List<ChatMessage> messagesToBeMarked = groupService.getMessagesToBeMarked(chatRoom.getId(), userId, toBeMarkedMessagesCount);
        String timestampStr = Instant.now().toString();
        Map<String, List<String>> messageIdsToBeMarked = new HashMap<>();

        for (ChatMessage messageToBeMarked : messagesToBeMarked) {
            if (isDelivered) {
                messageToBeMarked.addUserToDeliveredReceipt(timestampStr, userId);

                if (messageToBeMarked.getDeliveryReceiptsByTime().size() == chatRoom.getUserIds().size()) {
                    messageToBeMarked.setMessageStatus(MessageStatus.DELIVERED);
                    List<String> senderIdMessageIdsToBeMarked = messageIdsToBeMarked.getOrDefault(messageToBeMarked.getSenderId(), new ArrayList<>());
                    senderIdMessageIdsToBeMarked.add(messageToBeMarked.getId());
                    messageIdsToBeMarked.put(messageToBeMarked.getSenderId(), senderIdMessageIdsToBeMarked);
                }
            } else {
                messageToBeMarked.addUserToReadReceipt(timestampStr, userId);

                if (messageToBeMarked.getReadReceiptsByTime().size() == chatRoom.getUserIds().size()) {
                    messageToBeMarked.setMessageStatus(MessageStatus.READ);
                    List<String> senderIdMessageIdsToBeMarked = messageIdsToBeMarked.getOrDefault(messageToBeMarked.getSenderId(), new ArrayList<>());
                    senderIdMessageIdsToBeMarked.add(messageToBeMarked.getId());
                    messageIdsToBeMarked.put(messageToBeMarked.getSenderId(), senderIdMessageIdsToBeMarked);
                }
            }
        }

        chatMessageRepository.saveAll(messagesToBeMarked);

        for (Map.Entry<String, List<String>> entry : messageIdsToBeMarked.entrySet()) {
            String senderId = entry.getKey();
            List<String> senderIdMessageIdsToBeMarked = entry.getValue();
            webSocketMessageService.sendMarkedMessageStatus(chatRoom.getId(), senderId, senderIdMessageIdsToBeMarked, isDelivered);
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
