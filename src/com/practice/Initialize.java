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


        var message = " Live Update Microservice: Checking for Current Version File Information...";
        FileLog.logFile(message);

        if(!toppAppCurrentVersion.exists()) {

            message = " No Current Version File Information Found - Initializing...";
            FileLog.logFile(message);

            Config.isDatabaseInitialized = false;

            message = " Connecting to Local Database Instance...";
            FileLog.logFile(message);

            do {
                for (String string :
                        latestFiles) {
                    Version.copyLatestToCurrent(
                            latestPath + string,
                            currentVersionPath + string
                    );
                }

                if(toppAppCurrentVersion.exists()) {
                    message = " Connected to Local Database Instance -\n" +
                            " Current Version File Information Successfully Created";
                    FileLog.logFile(message);

                    Config.isDatabaseInitialized = true;

                } else {
                    message = " ERROR: Could Not Connect to Local Database Instance";
                    FileLog.logFile(message);

                    try {
                        message = " Attempting Again In 2,000 ms - Timeout in " + (timeoutTries + 1) + " Tries...";
                        FileLog.logFile(message);

                        Thread.sleep(2000);
                    } catch (InterruptedException ignore) {
                    }

                    if(timeoutTries == 0) {
                        message = " Database Connection Timed Out - Ending Live Update Daemon...";
                        FileLog.logFile(message);
                    }
                }

            } while (!Config.isDatabaseInitialized && timeoutTries-- > 0);

        } else {
            message = " Current Version File Information Found";
            FileLog.logFile(message);

            Config.isDatabaseInitialized = true;
        }
    }

}
