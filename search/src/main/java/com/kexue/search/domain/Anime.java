package com.kexue.search.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kexue.common.domain.BaseModel;
import com.kexue.common.util.PageUtils;
import com.kexue.search.exception.ServiceException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public IPage<Anime> selectListLikeName(String name, Long pageNum, Long pageSize) {
        if (StrUtil.isBlank(name)) {
            throw new ServiceException("请输入名称");
        }

        return new Anime().selectPage(
                PageUtils.startPage(pageNum, pageSize),
                new LambdaQueryWrapper<Anime>()
                        .like(Anime::getName, name)
                        .or()
                        .like(Anime::getAlias, name)
                        .orderByDesc(Anime::getSource));
    }


    public List<Anime> selectByIds(Set<Long> ids) {
        ArrayList<Anime> animes = new ArrayList<>();
        ids.forEach(id -> animes.add(selectById(id)));
        return animes;
    }
}
