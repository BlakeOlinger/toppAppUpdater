package com.practice;

import java.util.ArrayList;

class Config {
    static String programState = "0";
    static boolean isDatabaseInitialized = false;
    static ArrayList<Boolean> areUpdates = new ArrayList<>();
    static final String SOURCE_ROOT = Main.userRoot + "toppAppDBdaemon/programFiles/bin/";
    static final String SW_SOURCE_ROOT = Main.userRoot + "toppAppDBdaemon/programFiles/sw/";
    static final String TARGET_ROOT = Main.userRoot + "programFiles/bin/currentVersion/";
    static final String SW_TARGET_ROOT = Main.userRoot + "programFiles/bin/currentVersion/sw/";
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

    static final String[] SW_BIN_NAMES = {
            "hostfxr.dll",
            "hostpolicy.dll",
            "NLog.config",
            "SolidWorks.Interop.sldworks.dll",
            "SolidWorks.Interop.swconst.dll",
            "sw-part-auto-test.dll",
            "sw-part-auto-test.exe",
            "sw-part-auto-test.pdb",
            "sw-part-auto-test.runtimeconfig.dev.json",
            "sw-part-auto-test.runtimeconfig.json"
    };
}
