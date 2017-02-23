package com.axxes.io;

import com.axxes.model.Cache;
import com.axxes.model.Endpoint;
import com.axxes.model.Video;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetReader {

    private Map<Integer, Video> videos = new HashMap<>();
    private Map<Integer, Cache> caches = new HashMap<>();
    private Map<Integer, Endpoint> endpoints = new HashMap<>();

    public Collection<Video> getVideos() {
        return videos.values();
    }

    public Video getVideo(int videoId) {
        return videos.get(videoId);
    }

    public Cache getCache(int cacheId) {
        return caches.get(cacheId);
    }

    public Collection<Cache> getCaches() {
        return caches.values();
    }

    public Collection<Endpoint> getEndpoints() {
        return endpoints.values();
    }

    public void readData(File file) throws IOException {
        List<String> lines = FileUtils.readLines(file, Charset.forName("UTF-8"));
        int lineCounter = 0;


        String firstLine = lines.get(lineCounter++);
        System.out.println(firstLine);
        int numberOfVideos = Integer.parseInt(firstLine.split(" ")[0]);
        int endpoints = Integer.parseInt(firstLine.split(" ")[1]);
        int request_desc = Integer.parseInt(firstLine.split(" ")[2]);
        int numberOfCaches = Integer.parseInt(firstLine.split(" ")[3]);
        int cacheSize = Integer.parseInt(firstLine.split(" ")[4]);

        System.out.println("Number of videos to be read: " + numberOfVideos);
        System.out.println("Number of endpoints to be read: " + endpoints);
        System.out.println("Number of request descriptions to be read: " + request_desc);
        System.out.println("Number of caches to be read: " + numberOfCaches);
        System.out.println("Cache size: " + cacheSize);

        String videoLine = lines.get(lineCounter++);
        createVideos(numberOfVideos, videoLine);

        createCaches(numberOfCaches, cacheSize);


        for (int i = 0; i < endpoints; i++) {
            System.out.println("Reading endpoint " + i);
            lineCounter = readEndpoint(i, lines, lineCounter);
        }

        for (int i = 0; i < videos.size(); i++) {
            fillVideo(i, lines, lineCounter);
            lineCounter++;
        }
    }

    private void fillVideo(int entry, List<String> lines, int lineCounter) {
        String line = lines.get(lineCounter);

        int videoId = Integer.parseInt(line.split(" ")[0]);
        int endPointId = Integer.parseInt(line.split(" ")[1]);
        int nbRequests = Integer.parseInt(line.split(" ")[2]);

        Video video = videos.get(videoId);
        Map<Endpoint, Integer> popularity = video.getPopularity();
        if (popularity == null) {
            popularity = new HashMap<>();
            video.setPopularity(popularity);
        }
        popularity.put(endpoints.get(endPointId), nbRequests);
    }

    private void createVideos(int numberOfVideos, String videoLine) {
        String[] sizes = videoLine.split(" ");
        for (int i = 0; i < numberOfVideos; i++) {
            Video v = new Video(i, Integer.parseInt(sizes[i]));
            videos.put(i, v);
            //System.out.println("Added video " + v);
        }
    }

    private void createCaches(int numberOfCaches, int cacheSize) {
        for (int i = 0; i < numberOfCaches; i++) {
            Cache cache = new Cache(i, cacheSize);
            caches.put(i, cache);
            //System.out.println("Added cache " + cache);
        }
    }

    private int readEndpoint(int endpointId, List<String> lines, int lineCounter) {
        int line = lineCounter;
        String endpointLine = lines.get(line++);
        int latency = Integer.parseInt(endpointLine.split(" ")[0]);
        int nbCaches = Integer.parseInt(endpointLine.split(" ")[1]);

        System.out.println(latency + "/" + caches);
        Map<Integer, Integer> latencyMap = new HashMap<>();

        for (int i = 0; i < nbCaches; i++) {
            readCacheLatencies(line++, lines, endpointId, latencyMap);
        }

        endpoints.put(endpointId, new Endpoint(endpointId, latency, latencyMap));

        return line;
    }

    private void readCacheLatencies(int i, List<String> lines, int endpointId, Map<Integer, Integer> latencyMap) {
        String line = lines.get(i);
        int cacheId = Integer.parseInt(line.split(" ")[0]);
        int latency = Integer.parseInt(line.split(" ")[1]);
        latencyMap.put(cacheId, latency);
    }

}
