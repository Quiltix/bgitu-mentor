package com.bgitu.mentor.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public GenericJackson2JsonRedisSerializer redisSerializer(ObjectMapper objectMapper) {

    ObjectMapper mapper = objectMapper.copy();

    mapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance,
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY);

    return new GenericJackson2JsonRedisSerializer(mapper);
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration(
      GenericJackson2JsonRedisSerializer redisSerializer) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(5))
        .disableCachingNullValues()
        .serializeValuesWith(SerializationPair.fromSerializer(redisSerializer));
  }

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
      RedisCacheConfiguration defaultCacheConfiguration) {
    return builder ->
        builder
            .withCacheConfiguration(
                "popularMentors", defaultCacheConfiguration.entryTtl(Duration.ofMinutes(15)))
            .withCacheConfiguration(
                "popularArticles", defaultCacheConfiguration.entryTtl(Duration.ofMinutes(10)));
  }
}
