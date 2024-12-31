package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {
    private final IChatRoomRepository chatRoomRepository;
    private final ClearChatService clearChatService;
    private final DeleteChatService deleteChatService;

    public ChatRoom getChatRoomById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(chatRoomId));
    }

    public void processChatRoomToDB(ChatRoom chatRoom) {
        if (chatRoom.isDeletable()) {
            chatRoomRepository.deleteById(chatRoom.getId());
        } else {
            chatRoomRepository.save(chatRoom);
        }
    }

    public void clearChat(ChatRoom chatRoom, String userId) {
        clearChatService.clearChat(chatRoom, userId);
    }

    public void deleteChat(ChatRoom chatRoom, String userId) {
        deleteChatService.deleteChat(chatRoom, userId);
    }

    public void joinChat()

//    clearChat(ChatRoom,UserId) => Clear Chat uses chatRoom field updates it and saves it, it does not notify
//    deleteChat(ChatRoom,User)
//    getOnlineUsersOfChat(ChatRoom) UserService Needed
//    getMessagesOfChatRoom(ChatRoom,UserId) UserService Needed
//    createChat(List<> memberIds, chatCreationMetadata) UserService Needed
//    addMembers
//    removeMembers
//    sendNotificationMessage
//
}
