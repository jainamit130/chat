package com.amit.converse.chat.config.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisKeyExpirationListener redisKeyExpirationListener;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        Topic topic = new ChannelTopic("__keyevent@0__:expired");
        container.addMessageListener(new MessageListenerAdapter(redisKeyExpirationListener), topic);

        return container;
    }

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("redis-15503.c264.ap-south-1-1.ec2.redns.redis-cloud.com");
        configuration.setPort(15503);
        configuration.setUsername("default");
        configuration.setPassword("L8SZsbvLtlodxSU63gzyJBWEItPLq9HI");

        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().build();
        return new JedisConnectionFactory(configuration, jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new GenericToStringSerializer<>(String.class));
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Autowired
    @Lazy
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public Boolean configureNotifyKeyspaceEvents() {
        try (Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection()) {
            jedis.configSet("notify-keyspace-events", "KEA");
            return true;
        } catch (Exception e) {
            System.err.println("Error configuring Redis notify-keyspace-events: " + e.getMessage());
            return false;
        }
    }
}
