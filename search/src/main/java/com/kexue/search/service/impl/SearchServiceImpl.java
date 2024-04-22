package com.kexue.search.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kexue.common.domain.BaseModel;
import com.kexue.common.domain.PageInfo;
import com.kexue.common.util.DictUtils;
import com.kexue.common.util.PageUtils;
import com.kexue.search.domain.Anime;
import com.kexue.search.domain.AnimeType;
import com.kexue.search.domain.to.AnimeQto;
import com.kexue.search.domain.vo.AnimeVo;
import com.kexue.search.exception.ServiceException;
import com.kexue.search.service.SearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Override
    public PageInfo<AnimeVo> search(AnimeQto animeQto) {
        String name = animeQto.getName();
        String type = animeQto.getType();
        if (StrUtil.isBlank(name) && StrUtil.isBlank(type)) {
            throw new ServiceException("请输入查询参数！");
        }

        PageUtils.initPage(animeQto);
        if (StrUtil.isNotBlank(name)) {
            return searchByName(name, animeQto.getPageNum(), animeQto.getPageSize());
        }
        if (StrUtil.isNotBlank(type)) {
            return searchByType(type, animeQto.getPageNum(), animeQto.getPageSize());
        }

        return null;
    }

    private PageInfo<AnimeVo> searchByType(String type, Long pageNum, Long pageSize) {
        IPage<AnimeType> animeTypeIPage = new AnimeType().selectPage(type, pageNum, pageSize);
        List<AnimeType> types = animeTypeIPage.getRecords();

        if (CollectionUtil.isEmpty(types)) {
            return new PageInfo<>();
        }

        Set<Long> animeIds = types.stream().map(AnimeType::getAnimeId).collect(Collectors.toSet());
        List<Anime> animes = new Anime().selectByIds(animeIds);

        return fillType(animes,
                animeTypeIPage.getCurrent(),
                animeTypeIPage.getSize(),
                animeTypeIPage.getTotal());
    }

    private PageInfo<AnimeVo> searchByName(String name, Long pageNum, Long pageSize) {
        IPage<Anime> animeIPage = new Anime().selectListLikeName(name, pageNum, pageSize);
        List<Anime> animes = animeIPage.getRecords();

        if (CollectionUtil.isEmpty(animes)) {
            return new PageInfo<>();
        }

        //        animeVoPageInfo.setRecords(animeVos);
        return fillType(animes,
                animeIPage.getCurrent(),
                animeIPage.getSize(),
                animeIPage.getTotal());
    }

    /**
     * 填充动漫类型
     * @param animes 动漫列表
     * @param pageNum 当前页
     * @param pageSize 分页大小
     * @param total 总条数
     * @return 分页对象
     */
    private PageInfo<AnimeVo> fillType(List<Anime> animes, Long pageNum, Long pageSize, Long total) {
        Set<Long> animeIds = animes.stream().map(BaseModel::getId).collect(Collectors.toSet());
        List<AnimeType> animeTypes = new AnimeType().selectByAnimeIds(animeIds);

        List<AnimeVo> animeVos = BeanUtil.copyToList(animes, AnimeVo.class);
        PageInfo<AnimeVo> animeVoPageInfo = new PageInfo<>();
        animeVoPageInfo.setPageNum(pageNum);
        animeVoPageInfo.setPageSize(pageSize);
        animeVoPageInfo.setTotal(total);
        animeVoPageInfo.setRecords(animeVos);

        Map<Long, List<AnimeType>> typesMap = animeTypes.stream()
                .collect(Collectors.groupingBy(AnimeType::getAnimeId));

        animeVos.forEach(item -> {
            List<AnimeType> types = typesMap.get(item.getId());
            Set<Integer> codes = types.stream().map(AnimeType::getCode).collect(Collectors.toSet());
            item.setType(DictUtils.getTypesDictCache(codes));
        });
        return animeVoPageInfo;
    }
}
