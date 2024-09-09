package com.amit.converse.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoDBConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory databaseFactory,
            MongoCustomConversions customConversions,
            MongoMappingContext mappingContext) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(databaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);

        // Configure a replacement for dots in map keys (as dots are not allowed in MongoDB keys)
        converter.setMapKeyDotReplacement("_");

        // Remove _class field for documents
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        converter.setCustomConversions(customConversions);
        return converter;
    }
}