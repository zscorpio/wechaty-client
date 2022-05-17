package com.scorpio.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.scorpio.web.service.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Scorpio
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void zSetAdd(String key, String member, double score) {
        this.stringRedisTemplate.opsForZSet().add(key, member, score);
    }

    @Override
    public Set<String> zSetReverseRange(String key, long start, long end) {
        return this.stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @Override
    public void hSet(String key, String hKey, Object value) {
        stringRedisTemplate.opsForHash().put(key, hKey, value);
    }

    @Override
    public <T> List<T> hMultiGet(String key, Collection<Object> hKeys, Class<T> clz) {
        List<Object> data = stringRedisTemplate.opsForHash().multiGet(key, hKeys);
        if (CollectionUtils.isNotEmpty(data)) {
            return data.stream().map(str -> JSON.parseObject((String) str, clz)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
