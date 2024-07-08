package com.kexue.crawl.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;


@Component
public class AgeVideoProcessor implements PageProcessor {

    private String videoLink;

    private JSONArray videoLinks;
    
    private Site site = Site.me()
            .setSleepTime(1000)
            .setRetrySleepTime(1000)
            .setRetryTimes(3);

    @Override
    public void process(Page page) {
        // 解析播放视频页面
//        videoLink = page.getHtml().css("#playerIFrame iframe", "src").toString();
        
        // 解析视频 API 页面
        JSONObject jsonObject = JSON.parseObject(Jsoup.parse(page.getHtml().toString()).text());
        JSONObject playlists = (JSONObject) JSONPath.eval(jsonObject, "$.video.playlists");
        videoLinks = (JSONArray) playlists.get(playlists.keySet().iterator().next());
    }

    public String getVideoLink() {
        return videoLink;
    }
    
    public JSONArray getVideoLinks() {
        return videoLinks;
    }
}
