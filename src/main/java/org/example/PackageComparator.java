package org.example;

import java.util.Comparator;

class PackageComparator implements Comparator<Package> {

    @Override
    public int compare(Package p1, Package p2){
        if (!p1.getPriority().equals(p2.getPriority())) {
            return p1.getPriority().equals(PackagePriority.EXPRESS) ? -1 : 1; }
        if (p1.getDeadline() != p2.getDeadline()) {
            return Long.compare(p1.getDeadline(), p2.getDeadline());
        }
        return Long.compare(p1.getOrderTime(), p2.getOrderTime());
    }

}