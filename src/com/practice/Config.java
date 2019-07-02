package com.practice;

class Config {
    static String programState = "0";
    static boolean isDatabaseInitialized = false;
    static final String SOURCE_ROOT = "toppAppDBdaemon/programFiles/bin/";
    static final String TARGET_ROOT = "programFiles/bin/currentVersion/";
    static final String[] MICROSERVICE_NAMES = {
            "toppApp.jar",
            "toppAppDBdaemon.jar",
            "toppAppUpdater.jar",
            "toppAppMaster.jar"
    };
    static final String[] CONFIG_NAMES = {
            "GUI.config",
            "DBdaemon.config",
            "master.config"
    };

    public void run() {

                new LiveUpdate().checkAndUpdate();


    }
}
