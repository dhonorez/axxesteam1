package com.axxes.algorithm;

import lombok.Data;

@Data
public class Step implements Comparable {
    private final double latencyToBeWonPerMB;
    private final int videoId;
    private final int cacheId;

    public Step(int videoId, int cacheId, double latencyToBeWonPerMB) {
        this.videoId = videoId;
        this.cacheId = cacheId;
        this.latencyToBeWonPerMB = latencyToBeWonPerMB;
    }


    @Override
    public int compareTo(Object o) {
        if (o instanceof Step) {
            return (int) (this.latencyToBeWonPerMB - ((Step) o).getLatencyToBeWonPerMB());
        }
        return 0;
    }
}
