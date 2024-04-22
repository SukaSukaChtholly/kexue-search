package com.kexue.crawl;

import com.kexue.crawl.job.AnimeJob;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CrawlApplicationTests {

    @Resource
    AnimeJob animeJob;

    @Test
    void testType() {
        animeJob.typeProcess();
    }

    @Test
    void testAnime() {
        animeJob.AnimeProcess();
    }

}
