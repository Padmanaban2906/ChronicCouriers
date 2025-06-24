package org.example;

import java.util.List;

public class Rider {
    private int id;
    private RiderStatus status;
    private double reliabilityRating;
    private String location;

    public Rider() {
        this.status = RiderStatus.AVAILABLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RiderStatus getStatus() {
        return status;
    }

    public void setStatus(RiderStatus status) {
        this.status = status;
    }

    public double getReliabilityRating() {
        return reliabilityRating;
    }

    public void setReliabilityRating(double reliabilityRating) {
        this.reliabilityRating = reliabilityRating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
