package com.kexue.search.util;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.common.util.DictUtils;
import com.kexue.search.domain.Dict;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 动漫类型字典缓存
 *
 */
@Component
public class AnimeTypeCache implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Dict> types = new Dict().selectList(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getTypeId, 1));
        types.forEach(type -> DictUtils.setTypeDictCache(type.getName(), type.getCode()));
    }
}
