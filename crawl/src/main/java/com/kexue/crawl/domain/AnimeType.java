package com.kexue.crawl.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.common.domain.BaseModel;
import lombok.Data;

import java.util.Objects;


@Data
@TableName("t_anime_type")
public class AnimeType extends BaseModel<AnimeType> {

    private Integer code;
    private Long animeId;

    @Override
    public boolean insert() {

        AnimeType animeType = selectOne(new LambdaQueryWrapper<AnimeType>()
                .eq(AnimeType::getAnimeId, this.getAnimeId())
                .eq(AnimeType::getCode, this.code));

        if (Objects.nonNull(animeType)) return false;

        return super.insert();
    }

}
