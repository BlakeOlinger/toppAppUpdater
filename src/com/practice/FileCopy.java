package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileCopy implements Runnable {
    private final Thread thread;
    private final Path source;
    private final Path target;

    FileCopy(Path source, Path target) {
        this.source = source;
        this.target = target;
        thread = new Thread(this, "Copy Thread");
    }

    void copy() {
        thread.start();
    }

    @Override
    public void run() {
        try {
            Files.copy(source, target);
        } catch (IOException ignore) {
        }
    }
}
