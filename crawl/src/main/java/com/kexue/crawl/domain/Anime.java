package com.kexue.crawl.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.common.domain.BaseModel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;


@Data
@TableName("t_anime")
public class Anime extends BaseModel<Anime> {

    private String name;
    private String image;
    private String link;
    private String latest;
    private String alias;
    private String showTime;
    private String source;
    private Integer area;
    private Integer tag;
    private String info;


    public boolean insertOrUpdate() {
        Anime anime = this.selectOne(new LambdaQueryWrapper<Anime>()
                .eq(Anime::getLink, this.getLink()));

        if (Objects.isNull(anime)) {

            return this.insert();
        }

        // 如果动漫更新了，则更新数据
        BeanUtils.copyProperties(this, anime, "id");

        return anime.updateById();
    }


}
