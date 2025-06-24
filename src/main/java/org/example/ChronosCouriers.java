package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChronosCouriers {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File ridersFile = new File("Riders.json");
        List<Rider> riders = objectMapper.readValue(ridersFile, new TypeReference<List<Rider>>() {});


        File packagesFile = new File("Packages.json");
        List<Package> packages = objectMapper.readValue(packagesFile, new TypeReference<List<Package>>() {});



    }
}