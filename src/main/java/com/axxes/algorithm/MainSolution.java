package com.axxes.algorithm;

import com.axxes.model.Cache;
import com.axxes.model.Endpoint;
import com.axxes.model.Video;

import java.util.*;

public class MainSolution {

    private static Initializer initializer;

    public static void main(String[] args) {
        MainSolution mainSolution = new MainSolution();

        mainSolution.createSimpleSolution();
    }

    public MainSolution() {
        initializer = new Initializer();
        initializer.initializeData();
    }

    private void createSimpleSolution() {
        List<Cache> caches = initializer.getCaches();
        List<Video> videos = initializer.getVideos();
        List<List<Cache>> solutions = new ArrayList<>();

        for(int i =0; i < 100; i ++) {
            List<Video> newVideos = new ArrayList<>(videos);
            Collections.shuffle(newVideos);
            solutions.add(createOneInteration(new ArrayList<>(caches), newVideos));
        }

        for(List<Cache> solution: solutions) {
            long saving = calculateSavings(solution);
            System.out.println(saving);
        }
    }

    private void assignVideos(Iterator<Video> videoIterator, Cache cache) {
        Set<Video> videosCache = new HashSet<>();
        int currentCacheSize = 0;
        while(currentCacheSize < cache.getSize() && videoIterator.hasNext()) {
            Video next = videoIterator.next();
            currentCacheSize += next.getSize();
            videosCache.add(next);
        }
        cache.setVideos(videosCache);
    }

    private List<Cache> createOneInteration(List<Cache> caches, List<Video> videos) {
        Iterator<Video> videoIterator = videos.iterator();

        for(Cache cache: caches) {
            assignVideos(videoIterator, cache);
        }
        return caches;
    }

    private long calculateSavings(List<Cache> caches) {
        long savings = 0;
        for(Cache cache: caches) {
            Set<Video> videos = cache.getVideos();
            for(Video video: videos) {
                for(Endpoint endpoint: video.getPopularity().keySet()) {
                    int popularity = video.getPopularity().get(endpoint);
                    savings += calculateOneSaving(cache, endpoint, popularity);
                }
            }
        }
        return savings;
    }

    private int calculateOneSaving(Cache cache, Endpoint endpoint, int popularity) {
        return (endpoint.getLatencyDataCenter() - endpoint.getLatencyToCache().get(cache)) * popularity;
    }


}
