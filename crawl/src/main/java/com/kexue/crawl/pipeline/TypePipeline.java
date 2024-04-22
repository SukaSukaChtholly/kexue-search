package com.kexue.crawl.pipeline;

import com.kexue.crawl.domain.Dict;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;


@Component
public class TypePipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        ArrayList<String> types = resultItems.get("types");

        if (CollectionUtils.isEmpty(types)) return;

        new Dict().saveType(types, 1L);
    }
}
