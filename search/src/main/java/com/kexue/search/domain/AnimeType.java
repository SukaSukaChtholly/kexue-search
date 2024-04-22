package com.kexue.search.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kexue.common.domain.BaseModel;
import com.kexue.common.util.DictUtils;
import com.kexue.common.util.PageUtils;
import lombok.Data;

import java.util.List;
import java.util.Set;


@Data
@TableName("t_anime_type")
public class AnimeType extends BaseModel<AnimeType> {

    private Integer code;
    private Long animeId;


    /**
     * 根据动漫id搜索类型
     * @param animeIds
     * @return
     */
    public List<AnimeType> selectByAnimeIds(Set<Long> animeIds) {
        return selectList(new LambdaQueryWrapper<AnimeType>()
                .in(AnimeType::getAnimeId, animeIds));
    }

    /**
     * 查询动漫类型
     * @param type
     * @param pageNum
     * @param pageSize
     * @return
     */
    public IPage<AnimeType> selectPage(String type, Long pageNum, Long pageSize) {
        return selectPage(
                PageUtils.startPage(pageNum, pageSize),
                new LambdaQueryWrapper<AnimeType>()
                        .eq(AnimeType::getCode, DictUtils.getCodeDictCache(type)));

    }

}
