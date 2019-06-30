package com.practice;

import java.io.File;

class Initialize implements Runnable {
    private final Thread thread;

    Initialize() {
        thread = new Thread(this, "Initialize Current Version");
    }

    void currentVersionData() {
        thread.start();
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
            for (String string :
                    latestFiles) {
                Version.copyLatestToCurrent(
                        latestPath + string,
                        currentVersionPath + string
                        );
            }

            if(toppAppCurrentVersion.exists()) {
                System.out.println(" Current Version File Information Successfully Created");
            } else {
                System.out.println(" ERROR: Could Not Initialize Current Version File Information");
            }
        } else {
            System.out.println(" Current Version File Information Found");
        }
    }

}
