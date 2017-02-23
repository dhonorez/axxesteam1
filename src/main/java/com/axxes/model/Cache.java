package com.axxes.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Cache {

    private final int id;
    private final int size;

    private Set<Video> videos = new HashSet<>();

    public boolean canAddVideo(int toAdd) {
        int size = 0;
        for (Video video : videos) {
            size += video.getSize();
        }
        if (toAdd + size <= this.size) {
            return true;
        }
        return false;
    }

    public void addVideo(Video video) {
        videos.add(video);
    }
}
