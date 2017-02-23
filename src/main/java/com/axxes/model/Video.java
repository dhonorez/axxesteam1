package com.axxes.model;

import lombok.Data;

import java.util.Map;

@Data
public class Video {

    private final int id;
    private final int size;
    private final Map<Endpoint, Integer> popularity; // endpoint, views

}
