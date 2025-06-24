package org.example;

import exception.PackageNotFoundException;
import exception.RiderNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class DispatchCenter {
    private static final Logger logger = LogManager.getLogger(DispatchCenter.class);

    private Map<Integer, Rider> riders = new HashMap<>();
    private Map<Integer, Package> packages = new HashMap<>();

    private PriorityQueue<Package> packageQueue = new PriorityQueue<>(new PackageComparator());

    public void addRiderDetails(Rider rider) {
        riders.put(rider.getId(), rider);
        logger.info("Rider added: {}", rider.getId());
    }

    public void setPackages(Package pkg) {
        packageQueue.add(pkg);
        packages.put(pkg.getId(), pkg);
        logger.info("Package placed: {}", pkg.getId());
    }


    public void assignPackageToRider() throws RiderNotFoundException, PackageNotFoundException {
        if (packageQueue.isEmpty()) {
            throw new PackageNotFoundException("Packages not found");
        }
        if (riders.isEmpty()) {
            throw new RiderNotFoundException("Riders not found ");
        }
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
    }

    private void updateRiderAndPackageStatus(Rider rider, Package pkg) {
        rider.setStatus(RiderStatus.BUSY);
        pkg.setStatus(PackageStatus.ASSIGNED);
        logger.info("Package {} assigned to Rider {}", pkg.getId(), rider.getId());
    }

}
