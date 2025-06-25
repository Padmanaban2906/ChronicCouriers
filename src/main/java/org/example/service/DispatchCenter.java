package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.Package;

import org.example.models.PackageStatus;
import org.example.models.Rider;
import org.example.models.RiderStatus;


import java.util.*;
import java.util.stream.Collectors;

public class DispatchCenter {
    private static final Logger logger = LogManager.getLogger(DispatchCenter.class);

    private Map<Integer, Rider> riders = new HashMap<>();
    private Map<Integer, Package> packages = new HashMap<>();
    private Map<Integer, Package> riderMappingDet = new LinkedHashMap<>();
    private PriorityQueue<Package> packageQueue = new PriorityQueue<>(new PackageComparator());
    private List<Package> delivered = new ArrayList<>();

    public void addRiderDetails(Rider rider) {
        riders.put(rider.getId(), rider);
        logger.info("Rider added: {}", rider.getId());
    }

    public void setPackages(Package pkg) {
        packageQueue.add(pkg);
        packages.put(pkg.getId(), pkg);
        logger.info("Package placed: {}", pkg.getId());
    }


    public PriorityQueue<Package> assignPackageToRider() {
        Map<Integer, Rider> sortedRiders = riders.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(Rider::getReliabilityRating).reversed()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        Queue<Package> unassignedPkg = new LinkedList<>();

        while (!packageQueue.isEmpty()) {
            Package pkg = packageQueue.poll();
            if (pkg != null) {
                Optional<Map.Entry<Integer, Rider>> getDriverDetailsBasedOnAvailability = sortedRiders.entrySet().stream()
                        .filter(i -> i.getValue().getLocation().equals(pkg.getLocation())
                                && i.getValue().getStatus() == RiderStatus.AVAILABLE)
                        .findFirst();
                if (getDriverDetailsBasedOnAvailability.isPresent()) {
                    updateRiderAndPackageStatus(getDriverDetailsBasedOnAvailability.get().getValue(), pkg);
                }
                else {
                    logger.info("Riders are not available at this time for the package {}", pkg.getId());
                    unassignedPkg.offer(pkg);
                }
            }
        }
        riders = sortedRiders;
        packageQueue.addAll(unassignedPkg);
        return packageQueue;
    }

    private void updateRiderAndPackageStatus(Rider rider, Package pkg) {
        rider.setStatus(RiderStatus.BUSY);
        pkg.setStatus(PackageStatus.ASSIGNED);
        riderMappingDet.put(rider.getId(), pkg);
        delivered.add(pkg);
        logger.info("Package {} assigned to Rider {}", pkg.getId(), rider.getId());
    }

    public void deliveryCompletionStatus() {
        riderMappingDet.entrySet().forEach(ridPkg -> {
            if(!delivered.isEmpty()) {
                Rider rider = riders.get(ridPkg.getKey());
                rider.setStatus(RiderStatus.AVAILABLE);
                riders.replace(ridPkg.getKey(), rider);
                delivered.getFirst().setStatus(PackageStatus.DELIVERED);
                logger.info("Package {} has been delivered successfully by the Rider {}", delivered.getFirst().getId(), rider.getId());
                delivered = delivered.stream()
                        .filter(i -> i.getStatus() != PackageStatus.DELIVERED).collect(Collectors.toList());
            }
        });
    }

    public boolean availabilityChk() {
        for (Package pkg : packageQueue) {
            return riders.entrySet().stream().anyMatch(i -> i.getValue().getStatus() == RiderStatus.AVAILABLE && i.getValue().getLocation().equals(pkg.getLocation()));
        }
        return false;
    }
}
