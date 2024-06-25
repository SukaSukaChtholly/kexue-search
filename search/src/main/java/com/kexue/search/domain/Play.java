package com.kexue.search.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import com.kexue.common.domain.BaseModel;
import lombok.Data;

@Data
@TableName("t_play")
public class Play extends BaseModel<Play> {
    
    private String episode;
    private String link;
    private Long animeId;
}
