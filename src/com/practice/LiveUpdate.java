package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

class LiveUpdate implements Runnable{
    private final Thread thread;
    private final int updateIndex;
    private final Path configPath;
    private final Path source;
    private final Path target;
    private static final Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    LiveUpdate(int updateIndex) {
        this.updateIndex = updateIndex;
        thread = new Thread(this, "Live Update");

        if (updateIndex < Config.MICROSERVICE_NAMES.length) {
            configPath = Paths.get(Config.CONFIG_ROOT + Config.CONFIG_NAMES[updateIndex]);
            source = Paths.get(Config.SOURCE_ROOT + Config.MICROSERVICE_NAMES[updateIndex]);
            target = Paths.get(Config.TARGET_ROOT + Config.MICROSERVICE_NAMES[updateIndex]);
            Config.UPDATE_NAME = Config.MICROSERVICE_NAMES[updateIndex];
        } else {
            var SWindex = updateIndex - Config.MICROSERVICE_NAMES.length;
            configPath = Paths.get(Config.CONFIG_ROOT + Config.CONFIG_NAMES[3]);
            source = Paths.get(Config.SW_SOURCE_ROOT + Config.SW_BIN_NAMES[SWindex]);
            target = Paths.get(Config.SW_TARGET_ROOT + Config.SW_BIN_NAMES[SWindex]);
            Config.UPDATE_NAME = Config.SW_BIN_NAMES[SWindex];
        }
    }

    void update() {
        thread.start();
    }

    void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error Live Update Updating Thread Interrupted", e);
        }
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Live Update - Thread - Updating - "
                + Config.UPDATE_NAME + " - Start");

//        if(updateIndex != 3) {
//            sendKillCommand();
//
//            new FileCopy(source, target).copy();
//
//            sendStartCommand();
//
//            resetAreUpdates();
//        } else
//            sendMasterLiveUpdateUpdateCommand();

        logger.log(Level.INFO, "Live Update - Thread - Updating - "
                + Config.UPDATE_NAME + " - Exit");
    }

    private void sendMasterLiveUpdateUpdateCommand() {
        var path = Paths.get(Config.CONFIG_ROOT + Config.CONFIG_NAMES[2]);

        try {
            Files.writeString(path, "00");
        } catch (IOException ignore) {
        }
    }

    private void resetAreUpdates() {
        Config.areUpdates.add(Boolean.FALSE);
    }

    private void sendStartCommand() {
        try {
            Files.writeString(configPath, "01");

            // TODO - use .bat files instead - cmd.exe /c *.bat
            // TODO - wait approximately 3 seconds before sending start command
//            var process = new ProcessBuilder("cmd.exe", "/c",
//                    Config.MICROSERVICE_NAMES[updateIndex]).start();

        } catch (IOException ignore) {
        }
    }

    private void sendKillCommand() {

        try {
            Files.writeString(configPath, "11");
        } catch (IOException ignore) {
        }
    }

}
