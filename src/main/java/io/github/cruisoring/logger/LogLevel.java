package io.github.cruisoring.logger;

import java.util.Comparator;

public enum LogLevel {
    verbose("V"),
    debug("D"),
    info("I"),
    warning("W"),
    error("E"),

    none("N");

    public String label;
    LogLevel(String label){
        this.label = label;
    }
}

