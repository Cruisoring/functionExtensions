package io.github.cruisoring.logger;

import io.github.cruisoring.Lazy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class FileLogger extends Logger implements AutoCloseable {
    public static final String NEW_LINE = "\r\n";
    static final Consumer<String> _do_nothing = s -> {
    };
    final File file;
    boolean isValid = true;
    final Lazy<FileWriter> fileWriterLazy;
    final Lazy<BufferedWriter> bufferedWriterLazy;

    public FileLogger(String filePath, LogLevel minLevel) {
        super(_do_nothing, minLevel);
        this.file = getFile(filePath);
        this.fileWriterLazy = new Lazy<>(() -> new FileWriter(file, true));
        this.bufferedWriterLazy = fileWriterLazy.create(BufferedWriter::new);
    }

    public FileLogger(String filePath) {
        this(filePath, LogLevel.verbose);
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

        return bufferedWriterLazy.getValue() != null;
    }

    @Override
    public void save(String message) {
        if (!isValid) {
            return;
        }

        try {
            BufferedWriter bufferedWriter = bufferedWriterLazy.getValue();
            bufferedWriter.write(message + NEW_LINE);
            bufferedWriter.flush();
        } catch (IOException e) {
            isValid = false;
        }
    }

    @Override
    public void close() throws Exception {
        fileWriterLazy.close();
    }
}
