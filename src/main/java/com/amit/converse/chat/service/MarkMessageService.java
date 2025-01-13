package com.amit.converse.chat.service;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class MarkMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final GroupServiceOld groupServiceOld;

    public void markAllMessagesDelivered(String userId){
        User user = userService.getUser(userId);
        if (user != null) {
            Set<String> chatRoomIds = user.getChatRoomIds();
            if (chatRoomIds != null && !chatRoomIds.isEmpty()) {
                for(String chatRoomId: chatRoomIds){
                    ChatRoom chatRoom = groupServiceOld.getChatRoom(chatRoomId);
                    if(chatRoom.isExitedMember(userId))
                        markLastExitedMessage(chatRoom,userId,true);
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
        ChatRoom chatRoom = groupServiceOld.getChatRoom(chatRoomId);
        // if chatRoom is Exited then mark read the last exited message
        if(chatRoom.isExitedMember(userId))
            markLastExitedMessage(chatRoom,userId,false);
        Integer toBeMarkedMessagesCount=chatRoom.getUnreadMessageCount(userId);
        if(toBeMarkedMessagesCount>0)
            markAllMessages(chatRoom,userId,false,toBeMarkedMessagesCount);
    }

    private void markLastExitedMessage(ChatRoom chatRoom,String userId,Boolean isDelivered) {
        ChatMessage lastExitedMessage = chatMessageRepository.getLastExitedMessage(chatRoom.getId(),userId);
        if(lastExitedMessage==null){
            return;
        }
        boolean isMarked = isDelivered? lastExitedMessage.getDeliveredRecipients().contains(userId):lastExitedMessage.getReadRecipients().contains(userId);
        if(!isMarked) {
            if(isDelivered) {
                lastExitedMessage.setDeliveredRecipients(new HashSet<>(Collections.singleton(userId)));
            } else {
                lastExitedMessage.setReadRecipients(new HashSet<>(Collections.singleton(userId)));
            }
            chatMessageRepository.save(lastExitedMessage);
        }
    }

    @Transactional
    public void markAllMessages(ChatRoom chatRoom,String userId, Boolean isDelivered, Integer toBeMarkedMessagesCount) {
        List<ChatMessage> messagesToBeMarked = groupServiceOld.getMessagesToBeMarked(chatRoom.getId(), userId, toBeMarkedMessagesCount);
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
                messageToBeMarked.addUserToReadReceipt(userId);

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
        groupServiceOld.saveChatRoom(chatRoom);
        return;
    }

}
