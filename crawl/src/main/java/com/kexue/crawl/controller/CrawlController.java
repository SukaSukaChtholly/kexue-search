package com.kexue.crawl.controller;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.common.log.annotation.Log;
import com.kexue.crawl.domain.Video;
import com.kexue.crawl.processor.AgeVideoProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@RestController
public class CrawlController {
    
    @Resource
    private AgeVideoProcessor ageVideoProcessor;

    @Resource
    private ThreadPoolTaskExecutor customAsyncThreadPool;

//    @Log(info = "getVideoLink")
//    @GetMapping("/getVideoLink")
//    public String getVideoLink(@RequestParam("animeId") String animeId,
//                               @RequestParam("animeLink") String animeLink,
//                               @RequestParam("number") Integer number) {
//        // 访问视频播放页面获取
//        PlaywrightDownloader playwrightDownloader = new PlaywrightDownloader();
//        String url = new Video().getVideoLink(animeLink, number);
//        Spider.create(ageVideoProcessor)
//                .addUrl(url)
//                .thread(1)
//                .setDownloader(playwrightDownloader)
//                .run();
//        playwrightDownloader.close();
//
//        String videoLink = ageVideoProcessor.getVideoLink();
//        asyncSaveVideoLink(animeId, number, videoLink);
//
//        return videoLink;
//    } 
    
    @Log(info = "getVideoLink")
    @GetMapping("/getVideoLink")
    public String getVideoLink(@RequestParam("animeId") String animeId,
                               @RequestParam("animeLink") String animeLink,
                               @RequestParam("number") Integer number) {
        // 访问视频播放页面获取
        String url = new Video().getReqVideoApiLink(animeLink);
        Spider.create(ageVideoProcessor)
                .addUrl(url)
                .thread(1)
                .run();

        JSONArray videoLinks = ageVideoProcessor.getVideoLinks();
        JSONArray jsonArray = (JSONArray) videoLinks.get(number - 1);
        String videoLink = new Video().getVideoLink(jsonArray.get(1).toString());
        
        asyncSaveVideoLink(animeId, number, videoLink);

        return videoLink;
    }
    
    

    private void asyncSaveVideoLink(String animeId, Integer number, String videoLink) {
        customAsyncThreadPool.submit(() -> {
            try {
                Video video = new Video().selectOne(new LambdaQueryWrapper<Video>()
                        .eq(Video::getAnimeId, animeId)
                        .eq(Video::getNumber, number));
                if (Objects.nonNull(video.getLink())) return;
                
                log.info("开始保存视频链接，视频id[{}]", video.getId());
                video.setLink(videoLink);
                video.insertOrUpdate();
            } catch (Exception e) {
                log.error("保存视频链接发生异常，异常信息[{}]",e.getMessage(), e);
            }
        });
    }
}
