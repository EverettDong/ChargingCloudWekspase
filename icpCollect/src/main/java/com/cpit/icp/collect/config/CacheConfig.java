package com.cpit.icp.collect.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import com.github.benmanes.caffeine.cache.Caffeine;



@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport{

	@Autowired
	private RedisTemplate redisTemplate;
	
	
	@Override
	public CacheManager cacheManager() {
		return redisCacheManager();
	}

	
	@Bean
	public RedisCacheManager redisCacheManager(){
		return new RedisCacheManager(redisTemplate);
	}
	

	@Bean(name="caffeine")
	public CacheManager caffeineCacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		
		ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
		for(Caches c : Caches.values()){
			caches.add(new CaffeineCache(c.name(), 
					Caffeine.newBuilder().recordStats()
				.expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
				.maximumSize(c.getMaxSize())
				.build())
			);
		}
		
		cacheManager.setCaches(caches);

		return cacheManager;
	}
	
	
	
	public static final int DEFAULT_MAXSIZE = 10000;
	public static final int DEFAULT_TTL = 120;
	
	/**
	 * 定義cache名稱、超時時長（秒）、最大容量
	 * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
	 */
	public enum Caches{
		localCache();
		
		Caches() {
		}

		Caches(int ttl) {
			this.ttl = ttl;
		}

		Caches(int ttl, int maxSize) {
			this.ttl = ttl;
			this.maxSize = maxSize;
		}
		
		private int maxSize=DEFAULT_MAXSIZE;	//最大數量
		private int ttl=DEFAULT_TTL;		//过期时间（秒）
		
		public int getMaxSize() {
			return maxSize;
		}
		public int getTtl() {
			return ttl;
		}
	}

	

}
