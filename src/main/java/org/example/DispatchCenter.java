package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatchCenter {
    private static final Logger logger = LogManager.getLogger(DispatchCenter.class);

    private Map<Integer, Rider> riders = new HashMap<>();
    private Map<Integer, Package> packages = new HashMap<>();

}
