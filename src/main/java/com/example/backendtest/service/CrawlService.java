package com.example.backendtest.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public interface CrawlService {

    JSONObject getCrawls(File allCrawls) throws IOException, ParseException;
}
