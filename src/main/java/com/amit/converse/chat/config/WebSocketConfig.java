package com.amit.converse.chat.config;

import com.amit.converse.chat.config.util.SecurityContextUtil;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.JwtService;
import com.amit.converse.chat.service.User.UserService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@AllArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private ChatService chatService;

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
                ChatService.clearContext();
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor.getUser() == null) handleTokenValidationAndSetUser(accessor);

                UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) accessor.getUser();
                SecurityContextUtil.ensureContextFromPrincipal(authToken);
                User user = (User) authToken.getPrincipal();

                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    String destination = accessor.getFirstNativeHeader("destination");
                    String chatRoomId = null;

                    if (destination != null && destination.startsWith("/topic/chat")) {
                        chatRoomId = destination.substring(destination.lastIndexOf('/') + 1);

                        boolean isInitial = !"false".equals(accessor.getFirstNativeHeader("X-Initial-Subscribe"));

                        User effectiveUser = user;
                        if (!isInitial) {
                            effectiveUser = userDetailsService.loadUserByUserId(user.getUserId());
                        }

                        if (!isInitial && effectiveUser != null && effectiveUser.isExited(chatRoomId)) {
                            System.out.println("Blocked resubscription to exited chatRoomId: " + chatRoomId);
                            return null;
                        }
                    }
                }

                if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
                    System.out.println("Client unsubscribed. userId: " + user.getUserId());
                }

                if(StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    String userId = user.getUserId();
                    User freshUser = userDetailsService.loadUserByUserId(userId);
                    System.out.println("Disconnecting: "+ accessor.getUser().getName() + " transiting to offline!");
                    if(!user.isOffline()) freshUser.transit();
                    return message;
                }

                if(user.isOffline()) UserService.getUserContext().transit();
                return message;
            }

            private void handleTokenValidationAndSetUser(StompHeaderAccessor accessor) {
                String token = accessor.getFirstNativeHeader("token");
                if (token != null && token.startsWith("Bearer ")) {
                    String jwt = token.substring(7);
                    String userId = jwtService.extractId(jwt);
                    User user = userDetailsService.loadUserByUserId(userId);
                    if (jwtService.isTokenValid(jwt,user)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                user, null, Collections.emptyList()
                        );
                        accessor.setUser(authenticationToken);
                    }
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
