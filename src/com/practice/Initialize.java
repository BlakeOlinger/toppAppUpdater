package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Initialize implements Runnable {
    private final Thread thread;

    Initialize() {
        thread = new Thread(this, "Initialize Current Version");
    }

    void initialize() {
        thread.start();
    }

    @Override
    public void run() {
        var sourceRoot = "toppAppDBdaemon/programFiles/bin/";
        var targetRoot = "programFiles/bin/currentVersion/";

        var names = new String[] {
                "toppApp.jar",
                "toppAppDBdaemon.jar",
                "toppAppUpdater.jar",
                "toppAppMaster.jar"
        };

        // TODO - copying files can be heavy and can benefit from multithreading
        //  implement a multithreading solution and make isDB init - to
        //  isupdaterinit via arraylist<bool> as before so each process is
        //  independent

        if(!Files.exists(Paths.get(targetRoot + names[0]))) {
            Config.isDatabaseInitialized = false;
            var timeout = 3;

            do {
                for(String name: names) {
                    try {
                        Files.copy(Paths.get(sourceRoot + name),
                                Paths.get(targetRoot + name));
                    } catch (IOException ignore) {
                    }
                }


                if(!Files.exists(Paths.get(targetRoot + names[0]))) {

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
