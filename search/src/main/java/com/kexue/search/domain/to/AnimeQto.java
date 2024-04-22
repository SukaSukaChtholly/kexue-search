package com.kexue.search.domain.to;


import com.kexue.common.domain.PageParam;
import lombok.Data;

@Data
public class AnimeQto extends PageParam {

    private String name;
    private String type;
}
