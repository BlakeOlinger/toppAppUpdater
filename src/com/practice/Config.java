package com.practice;

class Config {
    static String programState = "0";
    static boolean isDatabaseInitialized = false;
    static boolean areUpdates = false;
    static boolean[] updateIndex = {false, false, false, false};
    static final String SOURCE_ROOT = Main.userRoot + "toppAppDBdaemon/programFiles/bin/";
    static final String TARGET_ROOT = Main.userRoot + "programFiles/bin/currentVersion/";
    static final String CONFIG_ROOT = Main.userRoot + "programFiles/config/";
    static final String[] MICROSERVICE_NAMES = {
            "toppApp.jar",
            "toppAppDBdaemon.jar",
            "toppAppMaster.jar",
            "toppAppUpdater.jar"
    };

    static final String[] CONFIG_NAMES = {
            "GUI.config",
            "DBdaemon.config",
            "master.config"
    };
}
