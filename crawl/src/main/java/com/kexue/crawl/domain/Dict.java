package com.kexue.crawl.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.common.domain.BaseModel;
import com.kexue.common.util.DictUtils;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Data
@TableName("t_dict")
public class Dict extends BaseModel<Dict> {

    private Long typeId;
    private String name;
    private Integer code;
    private String remark;


    public void saveType(String name, Long dictTypeId) {
        Dict dict = new Dict().selectOne(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getName, name));

        if (Objects.nonNull(dict)) return;

        Dict newDict = new Dict();
        newDict.setTypeId(dictTypeId);
        newDict.setName(name);

        Dict lastDict = new Dict().selectOne(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getTypeId, dictTypeId).orderByDesc(Dict::getCode));

        newDict.setCode(lastDict.getCode() + 1);
        newDict.fullWhenCreate();

        newDict.insert();

        // 加入缓存
        DictUtils.setTypeDictCache(name, newDict.getCode());
    }

    /**
     * 批量保存类型
     * @param names
     * @param dictTypeId
     */
    public void saveType(List<String> names, Long dictTypeId) {

        if (CollectionUtils.isEmpty(names)) return;

        for (String name : names) {
            Dict dict = new Dict();
            dict.setTypeId(dictTypeId);
            dict.setName(name);
            dict.fullWhenCreate();

            Dict lastDict = dict.selectOne(new LambdaQueryWrapper<Dict>()
                    .eq(Dict::getTypeId, dictTypeId).orderByDesc(Dict::getCode));

            if (Objects.isNull(lastDict)) {
                dict.setCode(0);
                dict.insert();
                continue;
            }

            if (lastDict.getName().equals(name)) {
                // TODO 抛异常
                continue;
            }

            dict.setCode(lastDict.getCode() + 1);
            dict.insert();
        }
    }

}
