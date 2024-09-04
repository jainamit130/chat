package com.amit.converse.chat.config;

import com.amit.converse.chat.service.ChatRoomDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(ChatRoomDataFetcher chatRoomDataFetcher) {
        return wiringBuilder -> wiringBuilder
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("getChatRoomsOfUser", chatRoomDataFetcher));
    }
}
