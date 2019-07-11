package com.practice;

import java.io.IOException;
import java.nio.file.*;
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
    private boolean isSWName = false;
    private int SWindex = 0;
    private String UPDATE_NAME = "";

    LiveUpdate(int updateIndex) {
        this.updateIndex = updateIndex;
        thread = new Thread(this, "Live Update");

        if (updateIndex < Config.MICROSERVICE_NAMES.length) {
            configPath = Paths.get(Config.CONFIG_ROOT + Config.CONFIG_NAMES[updateIndex]);
            source = Paths.get(Config.SOURCE_ROOT + Config.MICROSERVICE_NAMES[updateIndex]);
            target = Paths.get(Config.TARGET_ROOT + Config.MICROSERVICE_NAMES[updateIndex]);
            UPDATE_NAME = Config.MICROSERVICE_NAMES[updateIndex];

//            System.out.println("Update Jar MS Name Constructor value - " + UPDATE_NAME);
        } else {
            isSWName = true;
            SWindex = updateIndex - Config.MICROSERVICE_NAMES.length;
            configPath = Paths.get(Config.CONFIG_ROOT + Config.CONFIG_NAMES[3]);
            source = Paths.get(Config.SW_SOURCE_ROOT + Config.SW_BIN_NAMES[SWindex]);
            target = Paths.get(Config.SW_TARGET_ROOT + Config.SW_BIN_NAMES[SWindex]);
            UPDATE_NAME = Config.SW_BIN_NAMES[SWindex];

//            System.out.println("SW Index Constructor value - " + SWindex);
//
//            System.out.println("Update SW MS Name Constructor value - " + UPDATE_NAME);
        }
    }

    String getUpdateName() {
        return UPDATE_NAME;
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
                + UPDATE_NAME + " - Start");

        if(updateIndex != 3) {

            if (updateIndex == 0 ||
            updateIndex == 2)
                sendSoftKillCommand();
            else
                sendKillCommand();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Error Updater Thread Interrupted", e);
            }

//            System.out.println("source - " + source);
//
//            System.out.println("target - " + target);
//
//            System.out.println("update name " + UPDATE_NAME);
//
//            System.out.println("is SW Name - " + isSWName);
//
//            System.out.println("update SW Index = " + SWindex);
//
//            System.out.println("name for SW Index = " + Config.SW_BIN_NAMES[SWindex]);

            UPDATE_NAME = Config.SW_BIN_NAMES[SWindex];

//            System.out.println("name after re-binding - " + UPDATE_NAME);

            try {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

                if(!isSWName)
                    Files.copy(source, Paths.get(Main.userRoot + UPDATE_NAME),
                        StandardCopyOption.REPLACE_EXISTING);
                else
                    Files.copy(source, Paths.get(Main.userRoot + "programFiles/sw/" +
                            UPDATE_NAME), StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error Updater Could Not Copy Files to Update", e);
            }

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Error Updater Thread Interrupted", e);
            }

            if (updateIndex < 4)
                sendStartCommand();
            else if (updateIndex == Config.startOnUpdateIndex)
                sendStartCommand();

        } else
            sendMasterLiveUpdateUpdateCommand();

        logger.log(Level.INFO, "Live Update - Thread - Updating - "
                + UPDATE_NAME + " - Exit");
    }

    private void sendSoftKillCommand() {
        try {
            Files.writeString(configPath, "010");
        } catch (IOException ignore) {
        }
    }

    private void sendMasterLiveUpdateUpdateCommand() {
        var path = Paths.get(Config.CONFIG_ROOT + Config.CONFIG_NAMES[2]);

        try {
            Files.writeString(path, "001");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error Could Not Write Update Live Update Command to Master");
        }
    }

    private void sendStartCommand() {
        try {
                Files.writeString(configPath, "011");

                getBatName();

                var process = new ProcessBuilder("cmd.exe", "/c",
                        Config.BAT_START_ROOT + Config.START_BAT_NAME).start();

                process.waitFor();

                process.destroy();

        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error Could Not Send Start Command to " +
                    UPDATE_NAME, e);
        }
    }

    void getBatName() {
        if(updateIndex < 4)
            Config.START_BAT_NAME = UPDATE_NAME.substring(0,
                    UPDATE_NAME.length() - 4) + ".bat";
        else
            Config.START_BAT_NAME = "sw-part-auto-test.bat";
    }

    private void sendKillCommand() {

        try {
            Files.writeString(configPath, "111");
        } catch (IOException ignore) {
        }
    }

}
