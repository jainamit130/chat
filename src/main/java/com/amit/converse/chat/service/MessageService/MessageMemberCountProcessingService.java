package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class MessageMemberCountProcessingService {

    @Autowired
    private IMessageRepository messageRepository;

    public Map<String,List<Message>> processMemberCountOfMessages(GroupChat groupChat, List<User> users) {
        Map<Instant, List<String>> exitInstantToUsers = new HashMap<>();

        for (User user : users) {
            Instant exitInstant = groupChat.getLastAvailableInstant(user.getUserId());
            exitInstantToUsers.computeIfAbsent(exitInstant, k -> new ArrayList<>()).add(user.getUserId());
        }

        List<Instant> sortedExitInstants = new ArrayList<>(exitInstantToUsers.keySet());
        sortedExitInstants.sort(Comparator.reverseOrder());

        int cumulativeUserCount = 0;
        Instant to = Instant.now();

        Map<String, List<Message>> userToMessagesMap = new HashMap<>();

        for (Instant currentExit : sortedExitInstants) {
            List<String> usersAtThisExit = exitInstantToUsers.get(currentExit);
            cumulativeUserCount += usersAtThisExit.size();

            List<Message> messagesInWindow =
                    messageRepository.findMessagesOfChatBetween(groupChat.getId(), currentExit, to);

            for (Message msg : messagesInWindow) {
                msg.incrementMemberCount(cumulativeUserCount);
            }

            messageRepository.saveAll(messagesInWindow);
            to = currentExit;

            for(String userId: usersAtThisExit) {
                userToMessagesMap.put(userId,messagesInWindow);
            }
        }
        return userToMessagesMap;
    }

}
