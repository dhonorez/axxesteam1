package com.axxes.model;

import lombok.Data;

import java.util.Set;

@Data
public class Cache {

    private final int id;
    private final int size;

    private Set<Video> videos;

}
