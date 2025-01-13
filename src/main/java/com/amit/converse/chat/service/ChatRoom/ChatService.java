package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.exceptions.ConverseChatRoomNotFoundException;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.ChatRoom.IChatRoomRepository;
import com.amit.converse.chat.service.ClearChatService;
import com.amit.converse.chat.service.DeleteChatService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService<T extends IChatRoom> {
    @Autowired
    protected ChatContext<T> context;
    @Autowired
    protected MessageService messageService;
    @Autowired
    protected IChatRoomRepository chatRoomRepository;
    @Autowired
    protected ClearChatService clearChatService;
    @Autowired
    protected DeleteChatService deleteChatService;

    public void updateChatRoomContext(T chatRoom) {
        context.setChatRoom(chatRoom);
    }

    public void sendMessage(ChatMessage message) throws InterruptedException {
        messageService.sendMessage(message);
    }

    public IChatRoom getChatRoomById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseChatRoomNotFoundException(chatRoomId));
    }

    public void processChatRoomToDB(IChatRoom chatRoom) {
        if (chatRoom.isDeletable()) {
            chatRoomRepository.deleteById(chatRoom.getId());
        } else {
            chatRoomRepository.save(chatRoom);
        }
    }

    public void clearChat() {
        clearChatService.clearChat(context.getChatRoom());
        processChatRoomToDB(context.getChatRoom());
    }

    public void deleteChat() {
        deleteChatService.deleteChat(context.getChatRoom());
        processChatRoomToDB(context.getChatRoom());
    }

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
