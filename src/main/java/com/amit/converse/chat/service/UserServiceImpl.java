package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserEventDTO;
import com.amit.converse.common.SendMessageRequest;
import com.amit.converse.common.SendMessageResponse;
import com.amit.converse.common.User;
import com.amit.converse.common.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;

@GrpcService
@AllArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;

    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
        User grpcUser = request.getUser();
        try {
            Instant creationDate = Instant.ofEpochSecond(grpcUser.getCreationDate().getSeconds(), grpcUser.getCreationDate().getNanos());
            UserEventDTO userEventDTO = UserEventDTO.builder()
                    .userId(grpcUser.getUserId()).username(grpcUser.getUsername()).creationDate(creationDate).build();
            userService.consume(userEventDTO);

            SendMessageResponse response = SendMessageResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            SendMessageResponse response = SendMessageResponse.newBuilder().setSuccess(false).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}