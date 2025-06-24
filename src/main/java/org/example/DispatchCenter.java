package org.example;

import exception.RiderNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

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



}
