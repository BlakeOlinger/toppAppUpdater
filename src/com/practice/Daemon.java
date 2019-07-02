package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

class Daemon {
    private static final Path CONFIG_PATH = Paths.get("programFiles/config/updater.config");

    static void start(){
       var initialize = new Initialize();
       initialize.thread.start();

       try {
            initialize.thread.join();
        } catch (InterruptedException ignore) {
        }

        do {
            checkProgramState();

            checkForUpdates();

            if(Config.areUpdates) {
                for(var i = 0; i < 4; ++i) {
                    if(Config.updateIndex[i])
                        new LiveUpdate(i).update();
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        } while (Config.programState.compareTo("0") == 0);

    }

    private static void checkForUpdates() {
        byte[] sourceBytes;
        byte[] targetBytes;
        var index = 0;
        for(String name: Config.MICROSERVICE_NAMES) {
            try {
                sourceBytes = Files.readAllBytes(Paths.get(Config.SOURCE_ROOT + name));
                targetBytes = Files.readAllBytes(Paths.get(Config.TARGET_ROOT + name));

                if(!Arrays.equals(sourceBytes, targetBytes)) {
                    Config.updateIndex[index++] = true;
                }
            } catch (IOException ignore) {
            }
        }

        Config.areUpdates = true;
    }

    private static void checkProgramState() {
        try {
            Config.programState = Files.readString(CONFIG_PATH).substring(0,1);
        } catch (IOException ignore) {
        }
    }
}
