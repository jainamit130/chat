package com.amit.converse.chat.service.chatRoom.MessageFilters;

import com.amit.converse.chat.model.ChatRooms.BlindPeriod;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class GroupChatBlindPeriodFilter extends BlindPeriodFilter {
    @Override
    public <T extends Message> List<T> filterBlindSpots(ChatRoom chatRoom, User user, List<T> messages) {
        GroupChat groupChat = (GroupChat) chatRoom;
        List<BlindPeriod> blindPeriods = groupChat.getBlindPeriodsOfUser(user.getUserId());
        if(user.isExited(chatRoom.getId())) blindPeriods.add(new BlindPeriod(groupChat.getExitInstant(user.getUserId()), Instant.now()));
        return messages.stream()
                .filter(msg -> blindPeriods.stream().noneMatch(
                        period -> !msg.getTimestamp().isBefore(period.getStart()) &&
                                !msg.getTimestamp().isAfter(period.getEnd())
                ))
                .toList();
    }
}
