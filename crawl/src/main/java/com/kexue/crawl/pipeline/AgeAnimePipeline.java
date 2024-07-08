package com.kexue.crawl.pipeline;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.common.util.DictUtils;
import com.kexue.crawl.domain.Anime;
import com.kexue.crawl.domain.AnimeType;
import com.kexue.crawl.domain.Dict;
import com.kexue.crawl.domain.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class AgeAnimePipeline implements Pipeline {

    @Resource
    private ThreadPoolTaskExecutor customAsyncThreadPool;
    
    @Override
    public void process(ResultItems resultItems, Task task) {
        customAsyncThreadPool.submit(() -> {
            
        JSONArray videoLinks = resultItems.get("videos");
        if (Objects.nonNull(videoLinks)) {
            // 保存视频资源链接
            String name = resultItems.get("name");
            List<Video> videos = new Video().selectList(new LambdaQueryWrapper<Video>()
                    .eq(Video::getName, name)
                    .orderByAsc(Video::getNumber));
            for (int i = 0; i < videos.size(); i++) {
                try {
                    Video video = videos.get(i);
                    if (StrUtil.isNotBlank(video.getLink())) return;
                    JSONArray jsonArray = (JSONArray) videoLinks.get(i);
                    String videoLink = video.getVideoLink(jsonArray.get(1).toString());
                    video.setLink(videoLink);
                    video.updateById();
                    log.info("保存视频链接成功，动漫名称->[{}]，链接后缀->[{}]", name, video.getLink());
                } catch (Exception e) {
                    log.info("保存视频链接失败，动漫名称->[{}]，集数->[{}]，异常信息->[{}]", name, i, e);
                    return;
                }
            }
            return;
        }

            Anime anime = resultItems.get("anime");
            if (Objects.isNull(anime)) return;

            anime.insertOrUpdate();

            // 保存除 link 外的 video 信息
            List<String> episodes = resultItems.get("episodes");
            try {
                boolean flag = saveVideo(anime, episodes);
                if (!flag) return;
                log.info("保存动漫视频成功，名称-> [{}]，id->[{}]", anime.getName(), anime.getId());
            } catch (Exception e) {
                log.error("保存动漫视频失败，名称-> [{}]，id->[{}]，异常信息->[{}]", anime.getName(), anime.getId(), e.getMessage(), e);
                return;
            }

            String[] types = resultItems.get("types");
            if (ArrayUtil.isEmpty(types)) return;

            for (String type : types) {
                try {
                    // 如果是新类型，往字典添加值，加入缓存
                    new Dict().saveType(type, 1L);

                    AnimeType animeType = new AnimeType();
                    animeType.setAnimeId(anime.getId());
                    animeType.setCode(DictUtils.getCodeDictCache(type));
                    boolean flag = animeType.insert();
                    if (!flag) return;
                    log.info("保存动漫类型成功，类型 -> [{}]，动漫id -> [{}]", type, anime.getId());
                } catch (Exception e) {
                    log.error("保存动漫类型失败，类型 -> [{}]，动漫id -> [{}]，异常信息->[{}]", type, anime.getId(), e.getMessage(), e);
                    return;
                }
            }

            
        });
        
    }

    private boolean saveVideo(Anime anime, List<String> episodes) {
        if (CollectionUtils.isEmpty(episodes)) return false;

        List<Video> videos = new Video().selectList(new LambdaQueryWrapper<Video>()
                .eq(Video::getAnimeId, anime.getId()));
        
        if (videos.size() == episodes.size()) return false;
        
        if (!CollectionUtils.isEmpty(videos)) {
            new Video().insertVideos(anime, videos.size() + 1,
                    episodes.size(), episodes);
            return true;
        }
        
        new Video().insertVideos(anime,1, episodes.size(), episodes);
        return true;
    }
    
}
