package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.PackageNotFoundException;
import exception.RiderNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChronosCouriers {
    public static void main(String[] args) throws IOException, RiderNotFoundException, PackageNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();

        File ridersFile = new File("/Users/mnvspd/ChronicCouriers/ChronosCouriers/src/main/java/org/example/Riders.json");
        List<Rider> riders = objectMapper.readValue(ridersFile, new TypeReference<List<Rider>>() {});


        File packagesFile = new File("/Users/mnvspd/ChronicCouriers/ChronosCouriers/src/main/java/org/example/Packages.json");
        List<Package> packages = objectMapper.readValue(packagesFile, new TypeReference<List<Package>>() {});

        DispatchCenter dsp = new DispatchCenter();

        for (Rider rider : riders) {
            dsp.addRiderDetails(rider);
        }

        for (Package pkg : packages){
            dsp.setPackages(pkg);
        }

        dsp.assignPackageToRider();


    }
}