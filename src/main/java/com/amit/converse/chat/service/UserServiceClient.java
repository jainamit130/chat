package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserEventDTO;
import com.amit.converse.common.*;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@GrpcService
@AllArgsConstructor
public class UserServiceClient extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;

    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
        User user = request.getUser();

        Timestamp timestamp=user.getCreationDate();
        UserEventDTO userEvent = UserEventDTO.builder().userId(user.getUserId()).username(user.getUsername()).
                creationDate(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())).build();

        boolean success = userService.consume(userEvent);
        SendMessageResponse response = SendMessageResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
