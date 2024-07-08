package com.kexue.search.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import com.kexue.common.domain.BaseModel;
import lombok.Data;

@Data
@TableName("t_video")
public class Video extends BaseModel<Video> {
    
    private String episode;
    private int number;
    private String link;
    private String name;
    private Long animeId;
}
