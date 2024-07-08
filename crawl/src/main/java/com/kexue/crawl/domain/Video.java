package com.kexue.crawl.domain;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kexue.common.domain.BaseModel;
import lombok.Data;

import java.util.List;

@Data
@TableName("t_video")
public class Video extends BaseModel<Video> {
    
    private String episode;
    private int number;
    private String link;
    private String name;
    private Long animeId;

    public void insertVideos(Anime anime, int start, int end, List<String> episodes) {
        for (int i = start; i <= end; i++) {
            Video video = new Video();
            video.setName(anime.getName());
            video.setAnimeId(anime.getId());
            video.setEpisode(episodes.get(i - 1));
            video.setNumber(i);

            video.insert();
        }
    }

    public String getReqVideoLink(String url, int number) {
        if (StrUtil.isBlank(url)) return "";
        // eg: https://m.agedm.org/#/detail/20170031 变为 https://m.agedm.org/#/play/1/1
        return url.replace("detail", "play") + "/1/" + number;
    }

    public String getReqVideoApiLink(String url) {
        if (StrUtil.isBlank(url)) return "";
        // eg: https://m.agedm.org/#/detail/20170031 变为 https://api.agedm.org/v2/detail/20170031
        return url.replace("https://m.agedm.org/#", "https://api.agedm.org/v2");
    }

    public String getVideoLink(String uri) {
        return "https://43.240.156.118:8443/vip/?url=" + uri;
    }
    
}
