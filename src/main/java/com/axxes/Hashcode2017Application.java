package com.axxes;

import com.axxes.io.DatasetReader;
import com.axxes.model.Cache;
import com.axxes.model.Endpoint;
import com.axxes.model.Video;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class Hashcode2017Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Hashcode2017Application.class, args);

		DatasetReader datasetReader = new DatasetReader();
		datasetReader.readData(new ClassPathResource("me_at_the_zoo.in").getFile());

		System.out.println("Caches:");
		datasetReader.getCaches().forEach(System.out::println);

		System.out.println("Endpoints:");
		datasetReader.getEndpoints().forEach(System.out::println);

		System.out.println("Videos:");
		datasetReader.getVideos().forEach(System.out::println);
	}



}
