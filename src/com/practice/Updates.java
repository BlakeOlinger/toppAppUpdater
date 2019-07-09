package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class Updates implements Runnable{
    private final Thread thread;
    private static final Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final Path source;
    private final Path target;

    Updates(Path source, Path target) {
        this.source = source;
        this.target = target;
        thread = new Thread(this, "Update Check");
    }

    void check() {
        thread.start();
    }

    void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error Update Thread Interrupted", e);
        }
    }

    @Override
    public void run() {
        byte[] sourceBytes;
        byte[] targetBytes;

        try {
            sourceBytes = Files.readAllBytes(source);
            targetBytes = Files.readAllBytes(target);

            Config.areUpdates.add(!Arrays.equals(sourceBytes, targetBytes));

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error Could Not Read Files " + source +
                    " and " + target, e);

            Config.areUpdates.add(Boolean.FALSE);
        }
    }
}
