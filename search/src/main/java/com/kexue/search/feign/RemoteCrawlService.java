package com.kexue.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="crawl-service", url = "http://localhost:8081")
public interface RemoteCrawlService {
    
    @GetMapping("/getVideoLink")
    String getVideoLink(@RequestParam("animeId") Long animeId,
                        @RequestParam("animeLink") String animeLink,
                        @RequestParam("number") Integer number);
}
