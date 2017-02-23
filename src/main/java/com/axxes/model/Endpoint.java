package com.axxes.model;

import lombok.Data;

import java.util.Map;

@Data
public class Endpoint {

    private final int id;
    private final int latencyDataCenter;
    private final Map<Cache, Integer> latencyToCache;

}
