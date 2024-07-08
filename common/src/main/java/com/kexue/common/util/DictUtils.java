package com.kexue.common.util;


import com.kexue.common.cache.AppCacheService;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class DictUtils {

    public static void setTypeDictCache(String name, Integer code) {
        AppCacheService.set(name, code);
        AppCacheService.set(code, name);
    }

    /**
     * 通过 name 获取 code
     * @param name
     * @return code
     */
    public static Integer getCodeDictCache(String name) {
        return AppCacheService.get(name);
    }

    /**
     * 通过 code 获取 name
     * @param code
     * @return name
     */
    public static String getTypeDictCache(Integer code) {
        return AppCacheService.get(code);
    }

    public static Set<String> getTypesDictCache(Set<Integer> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptySet();
        }

        return codes.stream().map(DictUtils::getTypeDictCache).collect(Collectors.toSet());
    }

    public static Set<Integer> getCodesDictCache(Set<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return Collections.emptySet();
        }

        return names.stream().map(DictUtils::getCodeDictCache).collect(Collectors.toSet());
    }

}
