package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.exceptions.ConverseException;

public interface IRedisKeyService {
    default String extractPrefix(String keyValue) {
        int firstIndexOfDelimiter = keyValue.indexOf(':');
        if(firstIndexOfDelimiter==-1) throw new ConverseException("Invalid format in Redis keyValue: "+keyValue);
        return keyValue.substring(0,firstIndexOfDelimiter);
    }

    default String extractKey(String keyValue) {
        int firstIndexOfDelimiter = keyValue.indexOf(':');
        int lastIndexOfDelimiter = keyValue.lastIndexOf(':');
        if(firstIndexOfDelimiter==-1 || lastIndexOfDelimiter==-1 || firstIndexOfDelimiter==lastIndexOfDelimiter) throw new ConverseException("Invalid key found in Redis: "+keyValue);
        return keyValue.substring(firstIndexOfDelimiter+1,lastIndexOfDelimiter);
    }

    default String extractValue(String keyValue) {
        int lastIndexOfDelimiter = keyValue.lastIndexOf(':');
        if(keyValue.length()<=lastIndexOfDelimiter+1) throw new ConverseException("No Value found in Redis keyValue: "+keyValue);
        return keyValue.substring(lastIndexOfDelimiter+1);
    }
    String getPrefix();
    String getKey(String keyValue);
    String getKeyValue(String keyValue, String value);
}
