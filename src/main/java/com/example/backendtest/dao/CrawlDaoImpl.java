package com.example.backendtest.dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Repository
public class CrawlDaoImpl implements CrawlDao {
    @Override
    public JSONObject getSomeCrawls(File allCrawls) {
        JSONParser parser = new JSONParser();
        JSONObject outputObj = null;
        try {
            JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(allCrawls));

            String[] groups = {"HTTP_302", "HTTP_NO", "MIME_ALL", "abcde"};

            Map<String, List<Map<String, Integer>>> resultMap = new HashMap<>();

            for (String group : groups) {
                resultMap.put(group, new ArrayList<>());
            }

            for (Object crawlId : jsonObj.keySet()) {
                JSONObject crawlObj = (JSONObject) jsonObj.get(crawlId);
                for (Object crawlSubId : crawlObj.keySet()) {
                    Object crawlSubVal = crawlObj.get(crawlSubId);
                    if (crawlSubVal instanceof JSONObject) {
                        JSONObject crawlSubObj = (JSONObject) crawlSubVal;

                        Map<String, Integer> crawlMap = new HashMap<>();
                        for (String group : groups) {
                            if (crawlSubObj.containsKey(group)) {
                                int count = Integer.parseInt(crawlSubObj.get(group).toString());
                                crawlMap.put(group, count);
                            }
                        }
                        if (!crawlMap.isEmpty()) {
                            crawlMap.put("total", crawlMap.values().stream().mapToInt(Integer::intValue).sum());
                            crawlMap.put("totalInt", crawlMap.entrySet().stream().filter(e -> e.getKey().contains("_INT")).mapToInt(Map.Entry::getValue).sum());
                            crawlMap.put("totalExt", crawlMap.entrySet().stream().filter(e -> e.getKey().contains("_EXT")).mapToInt(Map.Entry::getValue).sum());

                            String crawlIdStr = crawlId.toString();
                            for (String group : groups) {
                                if (crawlMap.containsKey(group)) {
                                    List<Map<String, Integer>> groupList = resultMap.get(group);
                                    Optional<Map<String, Integer>> optional = groupList.stream().filter(e -> e.containsKey("crawlId") && e.get("crawlId").equals(crawlIdStr)).findFirst();
                                    if (optional.isPresent()) {
                                        Map<String, Integer> countMap = optional.get();
                                        for (Map.Entry<String, Integer> entry : crawlMap.entrySet()) {
                                            if (!entry.getKey().equals("crawlId")) {
                                                countMap.put(entry.getKey(), countMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
                                            }
                                        }
                                    } else {
                                        Map<String, Integer> newCrawlMap = new HashMap<>();
                                        newCrawlMap.putAll(crawlMap);
                                        newCrawlMap.put("crawlId", Integer.parseInt(crawlIdStr));
                                        groupList.add(newCrawlMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            outputObj = new JSONObject();
            for (String group : groups) {
                JSONArray groupArray = new JSONArray();
                List<Map<String, Integer>> groupList = resultMap.get(group);
                for (Map<String, Integer> crawlMap : groupList) {
                    JSONObject crawlObj = new JSONObject();
                    crawlObj.put("crawlId", crawlMap.get("crawlId").toString());
                    crawlObj.put("total", crawlMap.get(group));
                    crawlObj.put("totalInt", crawlMap.getOrDefault(group + "_INT", 0));
                    crawlObj.put("totalExt", crawlMap.getOrDefault(group + "_EXT", 0));
                    groupArray.add(crawlObj);
                }
                outputObj.put(group, groupArray);
            }

            System.out.println(outputObj.toString());

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return outputObj;
    }

    }

