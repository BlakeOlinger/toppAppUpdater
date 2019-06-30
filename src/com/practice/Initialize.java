package com.practice;

import java.io.File;

class Initialize implements Runnable {
    final Thread thread;

    Initialize() {
        thread = new Thread(this, "Initialize Current Version");
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        var timeoutTries = 2;
        var latestPath = "toppAppDBdaemon/programFiles/bin/";
        var currentVersionPath = "programFiles/bin/currentVersion/";

        var toppAppCurrentVersion = new File(currentVersionPath + "toppAppUpdater.jar");

        String[] latestFiles = {
                "toppApp.jar",
                "toppAppDBdaemon.jar",
                "toppAppUpdater.jar",
                "toppAppMaster.jar"
        };


        System.out.println(" Live Update Microservice: Checking for Current Version File Information...");
        if(!toppAppCurrentVersion.exists()) {

            System.out.println(" No Current Version File Information Found - Initializing...");
            Config.isDatabaseInitialized = false;

            System.out.println(" Connecting to Local Database Instance...");

            do {
                for (String string :
                        latestFiles) {
                    Version.copyLatestToCurrent(
                            latestPath + string,
                            currentVersionPath + string
                    );
                }

                if(toppAppCurrentVersion.exists()) {
                    System.out.println(" Connected to Local Database Instance -");
                    System.out.println(" Current Version File Information Successfully Created");
                    Config.isDatabaseInitialized = true;

                } else {
                    System.out.println(" ERROR: Could Not Connect to Local Database Instance");

                    try {
                        System.out.println(" Attempting Again In 2,000 ms - Timeout in " + (timeoutTries + 1) + " Tries...");
                        Thread.sleep(2000);
                    } catch (InterruptedException ignore) {
                    }

                    if(timeoutTries == 0) {
                        System.out.println(" Database Connection Timed Out - Ending Live Update Daemon...");
                    }
                }

            } while (!Config.isDatabaseInitialized && timeoutTries-- > 0);

        } else {
            System.out.println(" Current Version File Information Found");
            Config.isDatabaseInitialized = true;
        }
    }

}
