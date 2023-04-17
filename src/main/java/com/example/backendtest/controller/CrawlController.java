package com.example.backendtest.controller;

import com.example.backendtest.service.CrawlServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/crawls")
@CrossOrigin("*")

public class CrawlController {

    private final CrawlServiceImpl crawlServiceImpl;

    public CrawlController(CrawlServiceImpl crawlServiceImpl) {
        this.crawlServiceImpl = crawlServiceImpl;
    }

    @GetMapping("/list")
    public JSONObject listSomeCrawls(@RequestParam String filePath) throws IOException, ParseException {
        File allCrawls = new File(filePath);
        JSONObject someCrawls = crawlServiceImpl.getCrawls(allCrawls);
        return someCrawls;
    }
}
