package com.scorpio.web.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Author: Scorpio
 */
public interface RedisService {
    void zSetAdd(String key, String member, double score);

    Set<String> zSetReverseRange(String key, long start, long end);

    void hSet(String key, String hKey, Object value);

    <T> List<T> hMultiGet(String key, Collection<Object> hKeys, Class<T> clz);


}
