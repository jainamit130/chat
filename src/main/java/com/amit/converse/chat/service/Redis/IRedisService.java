package com.amit.converse.chat.service.Redis;

public interface IRedisService {
    default String getUserIdPrefix() { return "userId:"; }
    default String getChatRoomIdPrefix() { return "chatRoomId:"; }

    default String getChatRoomUserKey(String chatRoomId, String userId) {
        return getChatRoomIdPrefix()+":"+chatRoomId+":"+userId;
    }

    default String extractChatRoomIdFromUserKey(String key) {
        int lastIndexOfColon = key.lastIndexOf(':');
        if(lastIndexOfColon!=-1 && key.length()>lastIndexOfColon) {
            return key.substring(lastIndexOfColon);
        }
        return null;
    }

    default String extractUserIdFromUserKey(String key) {
        String userIdPrefix = getUserIdPrefix();
        int userIdPrefixLength = userIdPrefix.length();
        if(key.length()>userIdPrefixLength && key.substring(0,userIdPrefixLength).equals(userIdPrefix)) {
            int lastIndexOfColon = userIdPrefix.lastIndexOf(':');
            return key.substring(userIdPrefixLength,lastIndexOfColon);
        }
        return null;
    }

    default String getUserKeyPrefix(String userId) { return getUserIdPrefix()+userId+":"; }
}
