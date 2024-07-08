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
    private Integer playStatus;
    private String showTime;
    private String source;
    private Integer area;
    private Integer tag;
    private String info;


    public boolean insertOrUpdate() {
        Anime anime = this.selectOne(new LambdaQueryWrapper<Anime>()
                .eq(Anime::getSource, this.getSource())
                .eq(Anime::getName, this.getName()));
        
        if (Objects.isNull(anime) || !anime.getShowTime().equals(this.getShowTime())) {
            // 还有新版旧版同名问题
            return this.insert();
        }

        // 如果动漫更新了，则更新数据
        this.setId(anime.getId());
        BeanUtils.copyProperties(this, anime, "id");

        return anime.updateById();
    }


}
