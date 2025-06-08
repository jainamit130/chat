package com.amit.converse.chat.service.chatRoom.MessageFilters;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageFilterFactory {

    private final BlindPeriodFilter blindPeriodFilter;
    private final GroupChatBlindPeriodFilter groupChatBlindPeriodFilter;

    private final Map<ChatRoomType, BlindPeriodFilter> filterMap = new EnumMap<ChatRoomType, BlindPeriodFilter>(ChatRoomType.class);

    public BlindPeriodFilter getBlindPeriodFilter(ChatRoomType type) {
        if (filterMap.isEmpty()) {
            filterMap.put(ChatRoomType.GROUP, groupChatBlindPeriodFilter);
            filterMap.put(ChatRoomType.DIRECT, blindPeriodFilter);
            filterMap.put(ChatRoomType.SELF, blindPeriodFilter);
        }
        return filterMap.getOrDefault(type, blindPeriodFilter);
    }
}