package org.example;

import lombok.Data;

@Data
public class Package {
    private int id;
    private PackagePriority priority;
    private long deadline;
    private long orderTime;
    private PackageStatus status;
    private boolean isFragile;

    public Package() {
        this.status = PackageStatus.PENDING;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PackagePriority getPriority() {
        return priority;
    }

    public void setPriority(PackagePriority priority) {
        this.priority = priority;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }
}
