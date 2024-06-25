package com.kexue.search.service;


import com.kexue.common.domain.PageInfo;
import com.kexue.search.domain.Play;
import com.kexue.search.domain.to.AnimeQto;
import com.kexue.search.domain.vo.AnimeVo;

import java.util.List;

public interface SearchService {

    PageInfo<AnimeVo> search(AnimeQto animeQto);

    List<Play> getPlay(Long animeId);
}
