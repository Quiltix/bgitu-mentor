package com.bgitu.mentor.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisCacheConfiguration cacheConfiguration(ObjectMapper objectMapper) {

    ObjectMapper redisObjectMapper = objectMapper.copy();

    PolymorphicTypeValidator ptv =
        BasicPolymorphicTypeValidator.builder()
            .allowIfSubType(
                new BasicPolymorphicTypeValidator.TypeMatcher() {
                  @Override
                  public boolean match(MapperConfig<?> mapperConfig, Class<?> aClass) {
                    return aClass.getPackageName().startsWith("com.bgitu.mentor")
                        || aClass.isPrimitive()
                        || aClass.getName().startsWith("java.lang")
                        || aClass.getName().startsWith("java.util")
                        || aClass.getName().startsWith("java.time");
                  }
                })
            .build();

    redisObjectMapper.activateDefaultTyping(
        ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

    GenericJackson2JsonRedisSerializer redisValueSerializer =
        new GenericJackson2JsonRedisSerializer(redisObjectMapper);

    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(5))
        .disableCachingNullValues()
        .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(SerializationPair.fromSerializer(redisValueSerializer));
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
