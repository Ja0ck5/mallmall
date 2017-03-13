package com.mallmall.common.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mallmall.common.serviceI.ServiceFunction;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
	
	@Autowired(required = false)
	private ShardedJedisPool shardedJedisPool; 
	
	private <T> T execute(ServiceFunction<ShardedJedis, T> serviceFun){
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            // 执行逻辑
            return serviceFun.callback(shardedJedis);
        }finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
	}
	
	/*public String set(String key,String value){
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            // 从redis中获取数据
            return shardedJedis.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
	}*/
	
	/**
	 * 设置键值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(final String key,final String value){
		// 返回的参数 T 由当前函数返回类型决定
		return this.execute(new ServiceFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis jedis) {
				return jedis.set(key, value);
			}
		});
	}

	/**
	 * 获取值
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key){
		// 返回的参数 T 由当前函数返回类型决定
		return this.execute(new ServiceFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis jedis) {
				return jedis.get(key);
			}
		});
	}

	/**
	 * 根据 key 删除
	 * 
	 * @param key
	 * @return
	 */
	public Long del(final String key){
		// 返回的参数 T 由当前函数返回类型决定
		return this.execute(new ServiceFunction<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis jedis) {
				return jedis.del(key);
			}
		});
	}

	/**
	 * 设置生存时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(final String key,final Integer seconds){
		// 返回的参数 T 由当前函数返回类型决定
		return this.execute(new ServiceFunction<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis jedis) {
				return jedis.expire(key,seconds);
			}
		});
	}
	
	/**
	 * 设置值和生存时间
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public String set(final String key,final String value,final Integer seconds){
		// 返回的参数 T 由当前函数返回类型决定
		return this.execute(new ServiceFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis jedis) {
				String result = jedis.set(key, value);
				jedis.expire(key, seconds);
				return result;
			}
		});
	}
}
