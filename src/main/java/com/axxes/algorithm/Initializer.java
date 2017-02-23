package com.axxes.algorithm;

import com.axxes.model.Cache;
import com.axxes.model.Endpoint;
import com.axxes.model.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Initializer {

    private List<Endpoint> endPoints;
    private List<Video> videos;
    private List<Cache> caches;

    public void initializeData() {
        caches = createCaches();
        endPoints = createEndPoints(caches);
        videos = createVideos(endPoints);
    }

    private List<Cache> createCaches() {
        List<Cache> caches = new ArrayList<>();
        caches.add(new Cache(1, 5));
        caches.add(new Cache(2, 5));
        return caches;
    }

    private List<Video> createVideos(List<Endpoint> endPoints) {
        List<Video> videos = new ArrayList<>();
        HashMap<Endpoint, Integer> endpointMap = createEndpointMap(endPoints);
        videos.add(new Video(1, 10, endpointMap));
        videos.add(new Video(2, 15, endpointMap));
        videos.add(new Video(3, 20, endpointMap));
        return videos;
    }

    private HashMap<Endpoint, Integer> createEndpointMap(List<Endpoint> endPoints) {
        HashMap<Endpoint, Integer> popularity = new HashMap<>();
        for (Endpoint endpoint : endPoints) {
            popularity.put(endpoint, 5 * endpoint.getId());
            popularity.put(endpoint, 10 * endpoint.getId());
            popularity.put(endpoint, 15 * endpoint.getId());
        }
        return popularity;
    }

    private List<Endpoint> createEndPoints(List<Cache> caches) {
        List<Endpoint> endpoints = new ArrayList<>();
        endpoints.add(new Endpoint(1, 100, createLatencyToCache(caches)));
        endpoints.add(new Endpoint(2, 100, createLatencyToCache(caches)));
        endpoints.add(new Endpoint(3, 100, createLatencyToCache(caches)));
        return endpoints;
    }

    private HashMap<Cache, Integer> createLatencyToCache(List<Cache> caches) {
        HashMap<Cache, Integer> cacheIntegerHashMap = new HashMap<>();
        for(Cache cache: caches) {
            cacheIntegerHashMap.put(cache, 20 * cache.getId());
        }
        return cacheIntegerHashMap;
    }

    public List<Cache> getCaches() {
        return caches;
    }

    public List<Endpoint> getEndPoints() {
        return endPoints;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
