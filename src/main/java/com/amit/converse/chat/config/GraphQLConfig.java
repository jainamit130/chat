package com.amit.converse.chat.config;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

    @Bean
    public GraphQL graphQL() {
        GraphQLSchema schema = SchemaParser.newParser()
                .files("graphql/chat.graphqls") // Path to your schema file
                .build()
                .makeExecutableSchema();

        return GraphQL.newGraphQL(schema).build();
    }
}
