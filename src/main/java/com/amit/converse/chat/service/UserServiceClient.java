package com.amit.converse.chat.service;

import com.amit.converse.common.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceClient {
    private final ChatServiceGrpc.ChatServiceBlockingStub stub;

    public UserServiceClient(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9091)
                .usePlaintext()
                .build();
        stub = ChatServiceGrpc.newBlockingStub(channel);
    }

    public SendMessageResponse sendMessage(ChatMessage message) {
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setMessage(message)
                .build();
        SendMessageResponse response = stub.sendMessage(request);
        return response;
    }

    public GetMessagesResponse getMessages(String userId, String chatterUserId){
        GetMessagesRequest request = GetMessagesRequest.newBuilder()
                .setUserId(userId)
                .setChatWithUserId(chatterUserId)
                .build();
        return stub.getMessages(request);
    }
}
