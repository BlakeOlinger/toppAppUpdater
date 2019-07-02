package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Daemon {
    private static final Path CONFIG_PATH = Paths.get("programFiles/config/updater.config");

    static void start(){
       var initialize = new Initialize();
       initialize.thread.start();

       try {
            initialize.thread.join();
        } catch (InterruptedException ignore) {
        }

        var liveUpdateService = new LiveUpdate();

        do {
            checkProgramState();

            liveUpdateService.checkAndUpdate();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        } while (Config.programState.compareTo("0") == 0);

    }

    private static void checkProgramState() {
        try {
            Config.programState = Files.readString(CONFIG_PATH).substring(0,1);
        } catch (IOException ignore) {
        }
    }
}
