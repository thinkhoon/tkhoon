package com.tkhoon.framework.cache;

import com.tkhoon.framework.cache.impl.DefaultCacheManager;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        CacheManager cacheManager = DefaultCacheManager.getInstance();

        Cache cache = cacheManager.createCache("sample_cache");

        cache.put("A", 1);
        cache.put("B", 2);

        int a = (Integer) cache.get("A");
        System.out.println("a: " + a);

        List<Object> valueList1 = cache.getAll();
        System.out.println(valueList1.size());

        cache.remove("A");

        List<Object> valueList2 = cache.getAll();
        System.out.println(valueList2.size());

        Cache cache2 = cacheManager.createCache("sample_cache");

        List<Object> valueList3 = cache2.getAll();
        System.out.println(valueList3.size());
    }
}
