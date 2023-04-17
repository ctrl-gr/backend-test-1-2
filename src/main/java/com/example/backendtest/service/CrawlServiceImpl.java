package com.example.backendtest.service;

import com.example.backendtest.dao.CrawlDao;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class CrawlServiceImpl implements CrawlService {

    private final CrawlDao crawlDao;

    public CrawlServiceImpl(CrawlDao crawlDao) {
        this.crawlDao = crawlDao;
    }

    @Override
    public JSONObject getCrawls(File allCrawls) throws IOException, ParseException {
        return crawlDao.getSomeCrawls(allCrawls);
    }
}
