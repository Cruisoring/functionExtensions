package io.github.cruisoring.logger;

/**
 * Enum to specify the level of message to be kept.
 */
public enum LogLevel {
    verbose("V"),   //Most detailed level to log everything
    debug("D"),     //Detailed level to log information suitable for debugging purposes
    info("I"),      //Informative level to log major progresses
    warning("W"),   //Sensitive level to log warning messages
    error("E"),     //Critical level usually comes with Error or Exception

    none("N");      //Log nothing to turn off logging as a whole or of Logger instances

    public String label;

    LogLevel(String label) {
        this.label = label;
    }
}

