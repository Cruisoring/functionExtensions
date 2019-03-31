package io.github.cruisoring.logger;

public enum LogLevel {
    verbose("V"),
    debug("D"),
    info("I"),
    warning("W"),
    error("E");

    public String label;
    LogLevel(String label){
        this.label = label;
    }
}

