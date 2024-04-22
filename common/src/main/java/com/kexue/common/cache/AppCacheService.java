package com.kexue.common.cache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppCacheService {

    private static final Map<String, Integer> nameToCodeMap = new ConcurrentHashMap<>();
    private static final Map<Integer, String> codeToNameMap = new ConcurrentHashMap<>();


    public static void set(String key, Integer value) {
        nameToCodeMap.put(key, value);
    }

    public static void set(Integer code, String name) {
        codeToNameMap.put(code, name);
    }

    public static Integer get(String name) {
        return nameToCodeMap.get(name);
    }

    public static String get(Integer code) {
        return codeToNameMap.get(code);
    }
}
