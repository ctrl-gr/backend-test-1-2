package com.example.backendtest.dao;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public interface CrawlDao {

    JSONObject getSomeCrawls(File allCrawls) throws IOException, ParseException;
}
