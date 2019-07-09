package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class LiveUpdate implements Runnable{
    private final Thread thread;
    private final int updateIndex;
    private final Path configPath;
    private final Path source;
    private final Path target;

    LiveUpdate(int updateIndex) {
        this.updateIndex = updateIndex;
        thread = new Thread(this, "Live Update");

        configPath = Paths.get(Config.CONFIG_ROOT + Config.CONFIG_NAMES[updateIndex]);
        source = Paths.get(Config.SOURCE_ROOT + Config.MICROSERVICE_NAMES[updateIndex]);
        target = Paths.get(Config.TARGET_ROOT +  Config.MICROSERVICE_NAMES[updateIndex]);
    }

    void update() {
        thread.start();
    }

    @Override
    public void run() {
        if(updateIndex != 3) {
            sendKillCommand();

            new FileCopy(source, target).copy();

            sendStartCommand();

            resetAreUpdates();
        } else
            sendMasterLiveUpdateUpdateCommand();
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

            var process = new ProcessBuilder("cmd.exe", "/c",
                    Config.MICROSERVICE_NAMES[updateIndex]).start();

            process.waitFor();

            process.destroy();

        } catch (IOException | InterruptedException ignore) {
        }
    }

    private void sendKillCommand() {

        try {
            Files.writeString(configPath, "11");
        } catch (IOException ignore) {
        }
    }

}
