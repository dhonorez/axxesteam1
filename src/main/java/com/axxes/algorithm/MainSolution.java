package com.axxes.algorithm;

import com.axxes.io.DatasetReader;
import com.axxes.io.OutputWriter;
import com.axxes.model.Cache;
import com.axxes.model.Endpoint;
import com.axxes.model.Video;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainSolution {

    private static Initializer initializer;

    public static void main(String[] args) {
        MainSolution mainSolution = new MainSolution();
        DatasetReader datasetReader = new DatasetReader();
        try {
            datasetReader.readData(new File("src/main/resources/kittens.in"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainSolution.createSolution(datasetReader);
    }

    public MainSolution() {
        initializer = new Initializer();
        initializer.initializeData();
    }

    private void createSolution(DatasetReader datasetReader) {
        final Collection<Cache> caches = datasetReader.getCaches();
        final Collection<Video> videos = datasetReader.getVideos();
        final Collection<Endpoint> endPoints = datasetReader.getEndpoints();

        final List<Step> steps = createSteps(caches, videos, endPoints);
        final List<Step> stepsSorted = sortSteps(steps);
        System.out.println("STEPS SIZE: " + steps.size());
        doSteps(stepsSorted, datasetReader);

        long saving = calculateSavings(caches);
        System.out.println(saving);

        OutputWriter outputWriter = new OutputWriter();
        try {
            outputWriter.write(caches);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Step> createSteps(Collection<Cache> caches, Collection<Video> videos, Collection<Endpoint> endPoints) {
        List<Step> steps = new ArrayList<>();
        for (Video video : videos) {
            if (video.getPopularity() != null) {
                for (Endpoint endpoint : endPoints) {
                    final Integer requests = video.getPopularity().get(endpoint);
                    if (requests != null) {

                        Integer bestCacheId = null;
                        double bestLatencyToBeWonPerMB = Integer.MIN_VALUE;
                        for (Cache cache : caches) {
                            final Integer latencyToCache = endpoint.getLatencyToCache().get(cache.getId());
                            if (latencyToCache != null) { // IF CACHE IS REACHABLE
                                final int latencyToBeWon = requests * (endpoint.getLatencyDataCenter() - latencyToCache);
                                final double latencyToBeWonPerMB = ((double) latencyToBeWon) / video.getSize();
                                if (latencyToBeWonPerMB > bestLatencyToBeWonPerMB) {
                                    bestCacheId = cache.getId();
                                    bestLatencyToBeWonPerMB = latencyToBeWonPerMB;
                                }
                            }
                        }
                        System.out.println("adding a new step");
                        steps.add(new Step(video.getId(), bestCacheId, bestLatencyToBeWonPerMB));
                    }
                }
            }
        }
        return steps;
    }

    private List<Step> sortSteps(List<Step> steps) {
        return steps.stream().sorted().collect(Collectors.toList());
    }

    private void doSteps(List<Step> stepsSorted, DatasetReader datasetReader) {
        for (Step step : stepsSorted) {
            Cache cache = datasetReader.getCache(step.getCacheId());
            Video video = datasetReader.getVideo(step.getVideoId());
            if (cache.canAddVideo(video.getSize())) {
                System.out.println("Adding video " + video.getId() + " to cache " + cache.getId());
                cache.addVideo(video);
            }
        }
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

    private long calculateSavings(Collection<Cache> caches) {
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
        final Integer latencyToCache = endpoint.getLatencyToCache().get(cache.getId());
        if (latencyToCache == null)
            return 0;
        return (endpoint.getLatencyDataCenter() - latencyToCache) * popularity;
    }


}
