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
        if (args.length < 2 || args[0].isEmpty() || args[1].isEmpty()) {
            throw new IllegalArgumentException("Usage: java ChronosCouriers <packages_file_path> <riders_file_path>");
        }

        File packagesFile = new File(args[0]);
        File ridersFile = new File(args[1]);

        ObjectMapper objectMapper = new ObjectMapper();

        List<Rider> riders = objectMapper.readValue(ridersFile, new TypeReference<List<Rider>>() {});

        List<org.example.models.Package> packages = objectMapper.readValue(packagesFile, new TypeReference<List<org.example.models.Package>>() {});

        if (packages.isEmpty()) {
            throw new PackageNotFoundException("Packages not found");
        }
        if (riders.isEmpty()) {
            throw new RiderNotFoundException("Riders not found ");
        }
        DispatchCenter dsp = new DispatchCenter();

        boolean allRidersEmpty = true;
        for (Rider rider : riders) {
            if(!rider.getLocation().isEmpty()){
                dsp.addRiderDetails(rider);
                allRidersEmpty = false;
            } else {
                System.out.println("Rider location is empty for the ID : " + rider.getId() + " ,Hence we're skipping the details of the rider");
            }
        }

        if (allRidersEmpty) {
            throw new RiderNotFoundException("All riders have empty locations");
        }

        boolean allPackagesEmpty = true;
        for (Package pkg : packages){
            if(!pkg.getLocation().isEmpty()) {
                dsp.setPackages(pkg);
                allPackagesEmpty = false;
            } else {
                System.out.println("Package location is empty for the ID : " + pkg.getId() + ". Currently we're skipping the details of the package meanwhile please check customer and add the details of the location");
            }
        }

        if (allPackagesEmpty) {
            throw new PackageNotFoundException("All packages have empty locations");
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