package com.cpit.icp.pregateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public CacheManager cacheManager() {
		return redisCacheManager();
	}

	@Bean
	public RedisCacheManager redisCacheManager() {
		return new RedisCacheManager(redisTemplate);
	}

}
