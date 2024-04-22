package com.kexue.search.domain.vo;


import lombok.Data;

import java.util.Set;


@Data
public class AnimeVo {

    private Long id;
    private String name;
    private String image;
    private String link;
    private String latest;
    private String alias;
    private String showTime;
    private String source;
    private Set<String> type;
    private Integer area;
    private Integer tag;
    private String info;

}
