package com.kexue.crawl.processor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.common.enums.PlayStatusEnum;
import com.kexue.crawl.domain.Anime;
import com.kexue.crawl.domain.Video;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AgeAnimeProcessor implements PageProcessor {

    private Site site = Site.me()
            .setSleepTime(1000)
            .setRetrySleepTime(1000)
            .setRetryTimes(3);
    
    private AtomicInteger pageCount = new AtomicInteger(2);
    
    
    private int episodeCount;

    @Override
    public void process(Page page) {

        log.info("开始处理页面，链接 -> [{}]", page.getUrl());
        
        // 解析目录的所有记录
        Html html = page.getHtml();
        
        try {
            String text = Jsoup.parse(html.toString()).text();
            JSONArray videos = JSON.parseObject(text).getJSONArray("videos");
            
            if (Objects.isNull(videos) && checkPlayPage(text)) {
                // 此网页为视频 API
                saveVideo(text, page);
                return;
            }
            
            // 已经爬完
            if (Objects.isNull(videos) || videos.isEmpty()) {
                return;
            }
            
            for (int i = 0; i < videos.size(); i++) {
                JSONObject jsonObject = videos.getJSONObject(i);
                String href = jsonObject.getString("Href");
                
                if (!checkUpdate(jsonObject)) continue;
                // 如果未入库或已更新，进入详情页
                page.addTargetRequest("https://m.agedm.org/#" + href);
            }
        } catch (Exception e) {
            // 处理每部番详情信息
            saveAnime(page);
            // 获取请求视频 API
            page.addTargetRequest(new Video().getReqVideoApiLink(page.getUrl().toString()));
            return;
        }

        // 继续下一页
        page.addTargetRequest(StrUtil.format("https://api.agedm.org/v2/update?page={}&size=30", pageCount));
        pageCount.getAndAdd(1);
        System.out.println(pageCount);
    }
    
    /**
     * 解析动漫视频 API
     */
    private boolean checkPlayPage(String text) {
        // 根据是否有 video 字段判断是否是播放页
        try {
            JSONObject video = JSON.parseObject(text).getJSONObject("video");
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }

    private void saveVideo(String text, Page page) {

        JSONObject jsonObject = JSON.parseObject(text);
        String name = JSONPath.eval(jsonObject, "$.video.name").toString();
        page.putField("name", name);

        JSONArray videos = null;
        try {
            JSONObject playlists = (JSONObject) JSONPath.eval(jsonObject, "$.video.playlists");
            videos = (JSONArray) playlists.get(playlists.keySet().iterator().next());
        } catch (Exception e) {
            log.error("json转换异常，异常信息->[{}]", e.getMessage(), e);
            return;
        }

        page.putField("videos", videos);
    }

    /**
     * 解析动漫视频播放页
     */
//    private boolean isPlayPage(Html html) {
//        // 根据是否有简介判断是否是播放页
//        String iframe = html.css("#playerIFrame").get();
//        if (StrUtil.isBlank(iframe)) {
//            return false;
//        }
//        
//        return true;
//    }
//
//    private void saveVideo(Page page) {
//        Html html = page.getHtml();
//
//        Video video = new Video();
//        String name = html.css(".bd h2", "text").toString();
//        video.setName(name);
//        String link = html.css("#playerIFrame iframe", "src").toString();
//        video.setLink(link);
//        String url = page.getUrl().toString();
//        video.setNumber(Integer.parseInt(url.substring(url.lastIndexOf("/") + 1)));
//
//        page.putField("video", video);
//    }

    private boolean checkUpdate(JSONObject jsonObject) {
        // 如果未入库，或者已更新，需要保存
        String name = jsonObject.getString("Title");
        String latest = jsonObject.getString("NewTitle");
        Anime anime = new Anime().selectOne(new LambdaQueryWrapper<Anime>()
                .eq(Anime::getName, name)
                .eq(Anime::getSource, "age动漫"));
        
        if (Objects.isNull(anime)) return true;
        
        if (latest.equals(anime.getLatest())) {
            return false;
        }

        return true;
    }

//    @Async("CustomThreadPool")
    public void saveAnime(Page page) {
        Html html = page.getHtml();
        List<Selectable> detail = html.css(".video-detail-item").nodes();

        Anime anime = new Anime();
        String name = html.css("h5", "text").toString();
        anime.setName(name);
        String latest = detail.get(0).css(".van-tag", "text").toString();
        anime.setLatest(latest);
        anime.setLink(page.getUrl().toString());

        String img = detail.get(0).css("img", "src").toString();
        anime.setImage(img);
        
        List<Selectable> p = detail.get(0).css("p").nodes();
        String alias = Jsoup.parse(p.get(2).toString()).text();
        anime.setAlias(removePrefix(alias,"其他名称："));
        String showTime = Jsoup.parse(p.get(3).toString()).text();
        anime.setShowTime(removePrefix(showTime, "首播时间："));
        String status = Jsoup.parse(p.get(4).toString()).text();
        anime.setPlayStatus(PlayStatusEnum.getKey(removePrefix(status, "播放状态：")));
        
        String typeStr = Jsoup.parse(p.get(5).toString()).text();
        String[] types = removePrefix(typeStr, "剧情类型：").split(" ");
        page.putField("types", types);

        String info = Jsoup.parse(html.css(".videos-description").toString()).text();
        anime.setInfo(removePrefix(info, "简介："));

        anime.setSource("age动漫");

        List<Selectable> episodeHtml = html.css(".van-tabs__content .van-grid-item__text").nodes();
        List<String> episodes = new ArrayList<>();
        for (Selectable episode : episodeHtml) {
            episodes.add(episode.css("span", "text").toString());
        }
        episodeCount = episodes.size();
        page.putField("episodes",episodes);

        // TODO 地区、标签、状态等

        page.putField("anime", anime);
    }

    private String removePrefix(String str, String prefix) {
        return str.replace(prefix, "").trim();
    }

    @Override
    public Site getSite() {
        return site;
    }

}
