package com.kexue.crawl.downloader;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

@Slf4j
public class PlaywrightDownloader implements Downloader {
    
    private Browser browser;

    public PlaywrightDownloader() {
        Playwright playwright = Playwright.create();
        this.browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }
    
    @Override
    public Page download(Request request, Task task) {
        com.microsoft.playwright.Page page = browser.newPage();
        page.navigate(request.getUrl(), new com.microsoft.playwright.Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
        String content = page.content();
        page.close();

        Page webmagicPage = new Page();
        webmagicPage.setRawText(content);
        webmagicPage.setUrl(new PlainText(request.getUrl()));
        webmagicPage.setRequest(request);
        return webmagicPage;
    }

    @Override
    public void setThread(int i) {

    }

    public void close() {
        browser.close();
    }
}
