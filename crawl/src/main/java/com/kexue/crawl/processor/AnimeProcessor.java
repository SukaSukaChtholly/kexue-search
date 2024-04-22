package com.kexue.crawl.processor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kexue.crawl.domain.Anime;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Objects;


@Component
public class AnimeProcessor implements PageProcessor {

    private Site site = Site.me()
            .setSleepTime(3000)
            .setRetrySleepTime(1000)
            .setRetryTimes(3);

    @Override
    public void process(Page page) {

        // 解析目录的所有记录
        Html html = page.getHtml();
        List<Selectable> list = html.css("div.lpic ul li").nodes();

        if (CollectionUtils.isEmpty(list)) {
            // 处理每部番详情信息
            saveAnime(page);
            return;
        }

        for (Selectable selectable : list) {
            boolean isUpdate = checkUpdate(selectable);
            if (!isUpdate) continue;
            // 如果未入库或已更新，加入任务队列
            page.addTargetRequest(selectable.links().toString());
        }

        // 继续下一页
        List<Selectable> linkHtmls = html.css("div.fire div.pages a").nodes();

        for (Selectable linkHtml : linkHtmls) {
            if (linkHtml.toString().contains("下一页")) {
                page.addTargetRequest(linkHtml.links().toString());
                return;
            }
        }
    }

    private boolean checkUpdate(Selectable selectable) {
        // 如果已经入库，判断是否更新
        String link = selectable.links().toString();
        Anime anime = new Anime().selectOne(new LambdaQueryWrapper<Anime>()
                .eq(Anime::getLink, link));
        if (Objects.isNull(anime)) {
            return true;
        }

        String latest = selectable.css("span font", "text").toString();
        if (anime.getLatest().equals(latest)) {
            return false;
        }

        return true;
    }

    private void saveAnime(Page page) {
        Html html = page.getHtml();

        Anime anime = new Anime();
        anime.setName(html.css("div.rate h1", "text").toString());
        String alias = Jsoup.parse(html.css("div.sinfo p").nodes().get(0).toString()).text();
        anime.setAlias(removePrefix(alias, "别名:"));
        String type = Jsoup.parse(html.css("div.sinfo p").nodes().get(1).toString()).text();
        anime.setLatest(removePrefix(type, "更新至："));

        List<Selectable> spans = html.css("div.sinfo span").nodes();
        String time = Jsoup.parse(spans.get(0).toString()).text();
        anime.setShowTime(removePrefix(time,"上映:"));
        anime.setInfo(html.css(".info", "text").toString());
        anime.setSource("风车动漫");
        anime.setLink(html.css("div.thumb").links().toString());
        anime.setImage(html.css("div.thumb img", "src").toString());

        String typeStr = Jsoup.parse(spans.get(2).toString()).text();
        String[] types = removePrefix(typeStr, "类型:").split(" ");
        page.putField("type", types);

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
