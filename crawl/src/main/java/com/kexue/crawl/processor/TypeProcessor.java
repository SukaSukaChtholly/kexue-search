package com.kexue.crawl.processor;

import com.kexue.crawl.pipeline.TypePipeline;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TypeProcessor implements PageProcessor {

    private Site site = Site.me();

    @Resource
    private TypePipeline typePipeline;

    @Override
    public void process(Page page) {

        List<Selectable> list = page.getHtml().css("#search-list").nodes();

        list.forEach(System.out::println);

        if (CollectionUtils.isEmpty(list)) {
            String type = page.getHtml().regex("类型\",(.*?)]").toString();
            List<String> types = strConvert2Arr(type);
            page.putField("types", types);
            return;
        }

        for (Selectable selectable : list) {
            page.addTargetRequest(selectable.css("script", "src").toString());
        }



    }

    private List<String> strConvert2Arr(String str) {
        List<String> values = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            values.add(matcher.group(1));
        }
        return values;
    }

    @Override
    public Site getSite() {
        return site;
    }


}
