package com.kexue.search.controller;

import com.kexue.common.domain.PageInfo;
import com.kexue.common.domain.R;
import com.kexue.common.log.annotation.Log;
import com.kexue.search.domain.Video;
import com.kexue.search.domain.to.AnimeQto;
import com.kexue.search.domain.vo.AnimeVo;
import com.kexue.search.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

//@CrossOrigin(origins = "http://localhost")
@RestController
public class SearchController {

    @Resource
    private SearchService searchService;

    @Log(info = "search方法")
    @PostMapping("/search")
    public R<PageInfo<AnimeVo>> search(@RequestBody AnimeQto animeQto) {
        return R.ok(searchService.search(animeQto));
    }
    
    @Log(info = "getPlay方法")
    @GetMapping("/getPlayList")
    public R<List<Video>> getPlayList(Long animeId) {
        return R.ok(searchService.getPlayList(animeId));
    }
    
    @Log(info = "getVideo方法")
    @GetMapping("/getVideo")
    public R<Video> getVideo(Long animeId, Integer number) {
        
        return R.ok(searchService.getVideo(animeId, number));
    }
    
    
}

