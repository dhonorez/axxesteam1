package com.axxes;

import com.axxes.io.DatasetReader;
import com.axxes.model.Cache;
import com.axxes.model.Endpoint;
import com.axxes.model.Video;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
public class Hashcode2017Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Hashcode2017Application.class, args);

		DatasetReader datasetReader = new DatasetReader();
		datasetReader.readData(new ClassPathResource("me_at_the_zoo.in").getFile());

		System.out.println("Caches:");
		for (Cache cache : datasetReader.getCaches()) {
			System.out.println(cache);
		}

		System.out.println("Endpoints:");
		for (Endpoint endpoint : datasetReader.getEndpoints()) {
			System.out.println(endpoint);
		}

		System.out.println("Videos:");
		for (Video video : datasetReader.getVideos()) {
			System.out.println(video);
		}



	}



}
