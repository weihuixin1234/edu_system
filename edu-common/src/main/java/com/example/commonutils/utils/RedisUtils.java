package com.example.commonutils.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存并设置时间
     * @param key
     * @param value
     * @param expireTime
     * @param timeUnit
     * @return
     */
    public boolean set(final String key , Object value , Long expireTime , TimeUnit timeUnit){
        boolean result = false;
        try{
            ValueOperations<Serializable , Object> operations = redisTemplate.opsForValue();
            operations.set(key , value);
            redisTemplate.expire(key , expireTime , timeUnit);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys){
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     */
    public void removePatten(final String pattern){
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if(keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    //新加入的方法
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    //新加入的方法
    public boolean deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection) > 0;
    }


    //新加进去的方法
    /**
     * 获得缓存的基本对象列表
     */
    public Collection<String> keys(final String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key){
        if(exists(key)){
            redisTemplate.delete(key);
        }
    }

    public boolean exists(final String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    //get方法有修改
    public <T> T get(final String key){
        //Object result = null;
        ValueOperations<Serializable , T> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 哈希添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key , Object hashKey , Object value){
        HashOperations<String , Object , Object> hash = redisTemplate.opsForHash();
        hash.put(key , hashKey , value);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key , Object hashKey){
        HashOperations<String , Object , Object> hash = redisTemplate.opsForHash();
        return hash.get(key , hashKey);
    }

    /**
     *列表添加
     * @param k
     * @param v
     */
    public void lPush(String k , Object v){
        ListOperations<String , Object> list = redisTemplate.opsForList();
        list.rightPush(k , v);
    }

    /**
     * 列表获取
     * @return
     */
    public Object lRange(String k ,long l , long l1){
        ListOperations<String , Object> list = redisTemplate.opsForList();
        return list.range(k , l , l1);
    }

    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void add(String key , Object value){
        SetOperations<String , Object> set = redisTemplate.opsForSet();
    }

    public Set<Object> setMembers(String key){
        SetOperations<String , Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key , Object value , double scoure){
        ZSetOperations<String ,Object> zset = redisTemplate.opsForZSet();
        zset.add(key , value , scoure);
    }

    public Set<Object> rangeByScore(String key , double scoure , double scoure1){
        ZSetOperations<String ,Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key , scoure , scoure1);
    }
}
