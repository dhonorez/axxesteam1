package com.axxes;

import com.axxes.alg2.Calculator;
import com.axxes.io.DatasetReader;

import java.io.File;
import java.io.IOException;

public class Hashcode2017Application {

    public static void main(String[] args) throws IOException {

        DatasetReader datasetReader = new DatasetReader();
        datasetReader.readData(new File("src/main/resources/me_at_the_zoo.in"));

        new Calculator(datasetReader.getCaches(), datasetReader.getEndpoints(), datasetReader.getVideos()).calculate();
    }


}
