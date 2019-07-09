package com.practice;

import java.nio.file.Files;
import java.nio.file.Paths;

class Initialize implements Runnable {
    final Thread thread;

    Initialize() {
        thread = new Thread(this, "Initialize Current Version");
    }

    @Override
    public void run() {
        if(!Files.exists(Paths.get(Main.userRoot + "programFiles/bin/currentVersion/toppApp.jar"))) {

            Config.isDatabaseInitialized = false;

            var timeout = 3;

            do {
                for(String name: Config.MICROSERVICE_NAMES) {
                    new FileCopy(Paths.get(Config.SOURCE_ROOT + name),
                            Paths.get(Config.TARGET_ROOT + name)).copy();
                }


                if(Files.exists(Paths.get(Config.TARGET_ROOT + Config.MICROSERVICE_NAMES[0]))) {

                    Config.isDatabaseInitialized = true;

                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignore) {
                    }
                }

            } while (!Config.isDatabaseInitialized && timeout-- > 0);

        } else {
            Config.isDatabaseInitialized = true;
        }
    }

}
