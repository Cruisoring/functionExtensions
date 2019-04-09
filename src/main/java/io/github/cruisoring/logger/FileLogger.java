package io.github.cruisoring.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class FileLogger extends Logger implements AutoCloseable {
    final static Consumer<String> _do_nothing = s -> {
    };
    public static String NEW_LINE = "\r\n";
    final File file;
    boolean isValid = true;
    BufferedWriter bufferedWriter = null;
    public FileLogger(String filePath, LogLevel minLevel) {
        super(_do_nothing, minLevel);
        this.file = getFile(filePath);
    }

    static File getFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        return new File(Paths.get(filePath).toString());
    }

    @Override
    public String toString() {
        return String.format("FileLogger of %s or above with '%s'", minLevel, file);
    }

    @Override
    public boolean canLog(LogLevel level) {
        if (!isValid || !super.canLog(level)) {
            return false;
        }
        try {
            if (bufferedWriter == null) {
                FileWriter fileWritter = new FileWriter(file, true);
                bufferedWriter = new BufferedWriter(fileWritter);
            }
        } catch (IOException e) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void save(String message) {
        if (!isValid) {
            return;
        }

        try {
            if (bufferedWriter == null) {
                FileWriter fileWritter = new FileWriter(file, true);
                bufferedWriter = new BufferedWriter(fileWritter);
            }
            bufferedWriter.write(message + NEW_LINE);
            bufferedWriter.flush();
        } catch (IOException e) {
            isValid = false;
        }
    }

    @Override
    public void close() throws Exception {
        if (bufferedWriter != null) {
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }
}
