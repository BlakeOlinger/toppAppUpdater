package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

class Initialize implements Runnable {
    private final Thread thread;
    private static final Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    Initialize() {
        thread = new Thread(this, "Initialize Current Version");
    }

    void start() {
        thread.start();
    }

    void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error Initialize Thread Interrupted", e);
        }

        logger.log(Level.INFO, "Initialize Thread - Exit");
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Initialize Thread - Start");

        if(!Files.exists(Paths.get(Main.userRoot + "programFiles/bin/currentVersion/toppApp.jar"))) {
            logger.log(Level.INFO, "Initialize Thread - Current Version bin Not Found - Initialize - Start");

            Config.isDatabaseInitialized = false;

            var timeout = 3;

            do {

                if(Files.exists(Paths.get(Config.TARGET_ROOT + Config.MICROSERVICE_NAMES[0]))) {
                    Config.isDatabaseInitialized = true;

                } else {

                    for(String name: Config.MICROSERVICE_NAMES) {
                        new FileCopy(Paths.get(Config.SOURCE_ROOT + name),
                                Paths.get(Config.TARGET_ROOT + name)).copy();
                    }

                    for(String name : Config.SW_BIN_NAMES) {
                        try {
                            Files.copy(Paths.get(Config.SW_SOURCE_ROOT + name),
                                    Paths.get(Config.SW_TARGET_ROOT + name));
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, "Error Could Not Write SW Current Version Files", e);
                        }
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignore) {
                    }
                }

            } while (!Config.isDatabaseInitialized && timeout-- > 0);

            logger.log(Level.INFO, "Initialize Thread - Current Version bin Not Found - Initialize - Exit");

        } else {
            Config.isDatabaseInitialized = true;
        }
    }

}
