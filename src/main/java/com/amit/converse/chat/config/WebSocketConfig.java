package com.amit.converse.chat.config;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.context.User.SetUserContextService;
import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.service.JwtService;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@AllArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private ChatService chatService;
    private final SetUserContextService setUserContextService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()
                        .corePoolSize(10)
                                .maxPoolSize(20)
                                        .keepAliveSeconds(60);
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor.getUser() == null) handleTokenValidationAndSetUser(accessor);
                else userDetailsService.loadUserByUserId(accessor.getUser().getName());

                // Always transit to online in this flow
                if(UserContext.getUser().isOffline()) UserContext.getUser().transit();

                if(StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    System.out.println("Disconnecting: "+ accessor.getUser().getName() + " transiting to offline!");
                    UserContext.getUser().transit();
                }

                handleSetChatRoom(accessor);
                return message;
            }

            private void handleTokenValidationAndSetUser(StompHeaderAccessor accessor) {
                String token = accessor.getFirstNativeHeader("token");
                if (token != null && token.startsWith("Bearer ")) {
                    String jwt = token.substring(7);
                    if (jwtService.isTokenValid(jwt)) {
                        String userId = jwtService.extractId(jwt);
                        UserDetailsImpl userDetails = userDetailsService.loadUserByUserId(userId);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, Collections.emptyList()
                        );
                        accessor.setUser(authenticationToken);
                    }
                }
            }

            private void handleSetChatRoom(StompHeaderAccessor accessor) {
                String destination = accessor.getFirstNativeHeader("destination");
                if(destination!=null && destination.startsWith("/app/chat")) {
                    String chatRoomId = (destination.substring(destination.lastIndexOf('/')+1));
                    ChatContext.setChatRoom(chatService.getChatRoomById(chatRoomId));
                }
            }
        });

    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        // Avoid creating many ObjectMappers which have the same configuration.
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the Java Time module
        objectMapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(objectMapper);
        messageConverters.add(converter);

        // Don't add default converters.
        return true;
    }

}
