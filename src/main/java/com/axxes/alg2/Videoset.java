package com.axxes.alg2;

import com.axxes.model.Video;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Videoset {

    private int size; // MB
    private Set<Video> videos = new HashSet<>();
    private int maxSize;

    private int score;

    public Videoset() {

    }

    public Videoset(Videoset other) {
        if (other != null) {
            this.videos.addAll(other.videos);
            this.size = other.getSize();
            this.maxSize = other.getMaxSize();
        }
    }

    public boolean canAddVideo(Video v) {
        return (size + v.getSize()) <= maxSize;
    }

    public void addVideo(Video v) {
        size += v.getSize();
        videos.add(v);
    }

    public void addScore(int ms) {
        this.score += ms;
    }

}
