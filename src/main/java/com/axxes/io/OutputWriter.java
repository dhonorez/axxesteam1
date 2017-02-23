package com.axxes.io;

import com.axxes.model.Cache;

import java.io.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class OutputWriter {

    public void write(Collection<Cache> caches) throws IOException {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.txt"), "utf-8"));
        writer.write(caches.size() + "\n");
        for (Cache cache : caches) {
            final String videos = cache.getVideos().stream()
                    .map(video -> " " + video.getId())
                    .collect(Collectors.joining());
            writer.write(cache.getId() + " " + videos + "\n");
        }

        writer.close();
    }

}

