package com.kexue.crawl.pipeline;

import cn.hutool.core.util.ArrayUtil;
import com.kexue.common.util.DictUtils;
import com.kexue.crawl.domain.Anime;
import com.kexue.crawl.domain.AnimeType;
import com.kexue.crawl.domain.Dict;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Objects;


@Component
public class AnimePipeline implements Pipeline {


    @Override
    public void process(ResultItems resultItems, Task task) {
        Anime anime = resultItems.get("anime");
        if (Objects.isNull(anime)) return;

        anime.insertOrUpdate();

        String[] types = resultItems.get("type");
        if (ArrayUtil.isEmpty(types)) return;

        for (String type : types) {
            // 如果是新类型，往字典添加值，加入缓存
            new Dict().saveType(type, 1L);

            AnimeType animeType = new AnimeType();
            animeType.setAnimeId(anime.getId());
            animeType.setCode(DictUtils.getCodeDictCache(type));
            animeType.insert();
        }
    }
}
