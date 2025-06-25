package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.PackageNotFoundException;
import org.example.exception.RiderNotFoundException;
import org.example.models.Package;
import org.example.models.Rider;
import org.example.service.DispatchCenter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;

public class ChronosCouriers {
    public static void main(String[] args) throws IOException, RiderNotFoundException, PackageNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();

        File ridersFile = new File("/Users/mnvspd/ChronicCouriers/ChronosCouriers/src/main/java/org/example/Riders.json");
        List<Rider> riders = objectMapper.readValue(ridersFile, new TypeReference<List<Rider>>() {});


        File packagesFile = new File("/Users/mnvspd/ChronicCouriers/ChronosCouriers/src/main/java/org/example/Packages.json");
        List<org.example.models.Package> packages = objectMapper.readValue(packagesFile, new TypeReference<List<org.example.models.Package>>() {});

        if (packages.isEmpty()) {
            throw new PackageNotFoundException("Packages not found");
        }
        if (riders.isEmpty()) {
            throw new RiderNotFoundException("Riders not found ");
        }
        DispatchCenter dsp = new DispatchCenter();

        for (Rider rider : riders) {
            dsp.addRiderDetails(rider);
        }

        for (org.example.models.Package pkg : packages){
            dsp.setPackages(pkg);
        }

        PriorityQueue<Package> packageQueue = dsp.assignPackageToRider();
        while (!packageQueue.isEmpty()) {
            boolean availabilityChk = dsp.availabilityChk();
            if(availabilityChk){
                dsp.assignPackageToRider();
                dsp.deliveryCompletionStatus();
            } else {
                dsp.deliveryCompletionStatus();
            }
        }
    }
}