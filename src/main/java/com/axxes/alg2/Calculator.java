package com.axxes.alg2;

import com.axxes.model.Cache;
import com.axxes.model.Endpoint;
import com.axxes.model.Video;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dylanhonorez on 23/02/17.
 */
public class Calculator {

    private Set<Videoset> videosets = new HashSet<>();
    private final Set<Cache> cacheSet;
    private final Set<Endpoint> endpoints;
    private final Set<Video> videos;

    public Calculator(Set<Cache> cacheSet, Set<Endpoint> endpoints, Set<Video> videos) {
        this.cacheSet = cacheSet;
        this.endpoints = endpoints;
        this.videos = videos;
    }

    public void calculate() {
        Cache cache = cacheSet.iterator().next();

        Set<Video> optimized = createOptimized(videos);
        System.out.println("optimized videos: " + optimized.size());

        calculateVideoSets(cache, optimized);

        System.out.println("Calculated videosets: " + videosets.size());
        calculateScores(cache, endpoints, optimized);

        getHighestScore(cache);
    }

    private Set<Video> createOptimized(Set<Video> videos) {
        //videos.stream()
          //      .filter(v -> v.getPopularity()!= null )
            //    .sorted((v1, v2) -> Comparator.comparingInt())


        Set<Video> optimized = new HashSet<>();
        for (Video video : videos) {
            if (video.getPopularity() != null && !video.getPopularity().isEmpty()) {
                optimized.add(video);
            }
            if (optimized.size() == 10) {
                break;
            }
        }

        return optimized;

    }

    private void getHighestScore(Cache cache) {
        Videoset videoset = videosets.stream().sorted(Comparator.comparingInt(Videoset::getScore).reversed()).findFirst().get();
        System.out.println(videoset);
    }

    private void calculateScores(Cache cache, Set<Endpoint> endpoints, Set<Video> videos) {
        for (Video v: videos) {
            for (Videoset set : videosets) {
                if (set.getVideos().contains(v)) {
                    for (Endpoint endpoint : v.getPopularity().keySet()) {
                        set.addScore((endpoint.getLatencyDataCenter() - endpoint.getLatencyToCache().get(cache)) * v.getPopularity().get(endpoint));
                    }
                }
            }
        }
    }

    private void calculateVideoSets(Cache cache, Set<Video> videos) {
        for (Video v : videos) {
            System.out.println("Calculating for video " + v.getId() + " / " + videos.size() + " left");
            Videoset videoset = new Videoset();
            videoset.setMaxSize(cache.getSize());

            if (videoset.canAddVideo(v)) {
                videoset.addVideo(v);
                videosets.add(videoset);
                Set<Video> clone = new HashSet<>(videos);
                clone.remove(v);
                calculateForVideoSet(videoset, clone);
            }
        }
    }

    private void calculateForVideoSet(Videoset previous, Set<Video> videos) {
        for (Video v : videos) {
            System.out.println("Adding video " + v.getId() + " / " + videos.size() + " left");
            Videoset videoset = new Videoset(previous);
            if (videoset.canAddVideo(v)) {
                videoset.addVideo(v);
                videosets.add(videoset);
                Set<Video> clone = new HashSet<>(videos);
                clone.remove(v);
                calculateForVideoSet(videoset, clone);
            } else {
                System.out.println("SET FULL" + videoset.getVideos().size());
            }
        }
    }


}
