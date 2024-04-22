package com.kexue.search.service;


import com.kexue.common.domain.PageInfo;
import com.kexue.search.domain.to.AnimeQto;
import com.kexue.search.domain.vo.AnimeVo;

public interface SearchService {

    PageInfo<AnimeVo> search(AnimeQto animeQto);
}
