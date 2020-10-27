package com.sloopsight.sandbox.app.services;

import java.util.LinkedList;
import java.util.List;

import com.sloopsight.sandbox.app.meta.Intellisense;
import com.sloopsight.sandbox.app.meta.MethodHint;
import com.sloopsight.sandbox.app.meta.ParamHint;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Intellisense("localDb")
public class LocalDb {
    private static final CacheManager cacheManager = CacheManager.create();
    private Cache cache;

    public LocalDb(Long project) {
        this.cache = cacheManager.getCache("project_" + project);
    }

    @MethodHint(name = "set", comment = "set value to key with TTL in Seconds")
    public void set(@ParamHint("key") String key, @ParamHint("value as object") Object value,
            @ParamHint("Time To Live in Seconds") int ttl) {
        cache.put(new Element(key, value, 24 * 3600, ttl));
    }

    @MethodHint(name = "set", comment = "set value to key for 24 hours")
    public void set(@ParamHint("key") String key, @ParamHint("valye as object") Object value) {
        cache.put(new Element(key, value, 24 * 3600, 24 * 3600));
    }

    @MethodHint(name = "get", comment = "get value for key")
    public Object get(@ParamHint("key") String key) {
        return cache.get(key);
    }

    @MethodHint(name = "getAll", comment = "get value as (net.sf.ehcache.Element) in db")
    public List<Element> getAll() {
        List<Element> entires = new LinkedList<>();
        for (Object key : cache.getKeys()) {
            entires.add(cache.get(key));
        }
        return entires;
    }
}
