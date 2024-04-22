package com.kexue.crawl.job;


import com.kexue.crawl.pipeline.AnimePipeline;
import com.kexue.crawl.pipeline.TypePipeline;
import com.kexue.crawl.processor.AnimeProcessor;
import com.kexue.crawl.processor.TypeProcessor;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;

@Component
public class AnimeJob {

    @Resource
    private AnimePipeline animePipeline;

    @Resource
    private TypePipeline typePipeline;


    /**
     * 风车动漫爬取
     */
//    @Scheduled(cron = "0 */30 * * * *")
    public void AnimeProcess() {
//        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader
//                .setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1", 8800)));

        Spider.create(new AnimeProcessor())
                // 从最近更新开始爬取
                .addUrl("https://www.d53w.com/list/?order=%E6%9B%B4%E6%96%B0%E6%97%B6%E9%97%B4")
                // 不能使用布隆过滤，因为动漫更新一集页面变化太小
//                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                .thread(10)
                .addPipeline(animePipeline)
//                .setDownloader(httpClientDownloader)
                .run();
    }

    //    @Scheduled(initialDelay = 1000, fixedDelay = 60 * 60 * 1000)
    public void typeProcess() {

        Spider.create(new TypeProcessor())
                .addUrl("https://www.dm530w.org/list/")
                .thread(10)
                .addPipeline(typePipeline)
                .run();
    }

}
