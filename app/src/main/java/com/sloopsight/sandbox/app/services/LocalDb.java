package com.sloopsight.sandbox.app.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sloopsight.sandbox.app.meta.Intellisense;
import com.sloopsight.sandbox.app.meta.MethodHint;
import com.sloopsight.sandbox.app.meta.ParamHint;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

@Intellisense("localDb")
public class LocalDb {
    private static final CacheManager cacheManager = CacheManager.create();
    private Ehcache cache;

    public LocalDb(Long project) {
        this.cache = cacheManager.addCacheIfAbsent("project_" + project);
    }

    @MethodHint(name = "dbSetWithTtl", comment = "set value to key with TTL in Seconds")
    public void set(@ParamHint("key") String key, @ParamHint("value as object") Object value,
            @ParamHint("Time To Live in Seconds") int ttl) {
        cache.put(new Element(key, value, 24 * 3600, ttl));
    }

    @MethodHint(name = "dbSet", comment = "set value to key for 24 hours")
    public void set(@ParamHint("key") String key, @ParamHint("valye as object") Object value) {
        cache.put(new Element(key, value, 24 * 3600, 24 * 3600));
    }

    @MethodHint(name = "dbGet", comment = "get value for key")
    public Map<String, Object> get(@ParamHint("key") String key) {
        Element element = cache.get(key);
        if (element != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("ttl", element.getTimeToLive());
            result.put("value", element.getObjectValue());
        }
        return null;
    }

    @MethodHint(name = "getAll", comment = "get value as ({\"ttl\":\"time to live\",\"value\":\"Value stored \"}) in db")
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> entires = new LinkedList<>();
        for (Object key : cache.getKeys()) {
            Element element = cache.get(key);
            Map<String, Object> result = new HashMap<>();
            result.put("ttl", element.getTimeToLive());
            result.put("value", element.getObjectValue());
        }
        return entires;
    }
}
