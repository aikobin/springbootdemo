package com.bin.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	// @Value("${spring.redis.timeout}")
	// private int timeout;

	// 自定义缓存key生成策略
	// @Bean
	// public KeyGenerator keyGenerator() {
	// return new KeyGenerator(){
	// @Override
	// public Object generate(Object target, java.lang.reflect.Method method,
	// Object... params) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(target.getClass().getName());
	// sb.append(method.getName());
	// for(Object obj:params){
	// sb.append(obj.toString());
	// }
	// return sb.toString();
	// }
	// };
	// }

	// 缓存管理器
	@Bean
	public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		// 设置缓存过期时间
		cacheManager.setDefaultExpiration(10000);
		return cacheManager;
	}

	/**
	 * @Description: 防止redis入库序列化乱码的问题
	 * @return 返回类型
	 * @date 2018/4/12 10:54
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());// key序列化
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class)); // value序列化
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/*
	 * private void setSerializer(StringRedisTemplate template) {
	 * 
	 * @SuppressWarnings({ "rawtypes", "unchecked" })
	 * Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
	 * Jackson2JsonRedisSerializer(Object.class); ObjectMapper om = new
	 * ObjectMapper(); om.setVisibility(PropertyAccessor.ALL,
	 * JsonAutoDetect.Visibility.ANY);
	 * om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	 * jackson2JsonRedisSerializer.setObjectMapper(om);
	 * template.setValueSerializer(jackson2JsonRedisSerializer); }
	 */
}
