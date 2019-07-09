package com.practice;

/*
The purpose of this microservice is to provide a live updater
environment to the other microservices. This microservice monitors
it's programFiles/bin/currentVersion and diffs against the server's
toppAppDBdaemon/programFiles/bin/ live updates version. When a diff
returns true this microservice copies the live version to the stored
current version - sends the kill command to the given microservice -
and copies its current version to the production bin and restarts the
updated microservice via cmd.exe call
 */

// TODO - use .bat files from simple notepad programs to
//  - start microservices after update - JVM can't be trusted

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static final String userRoot = "C:/Users/bolinger/Desktop/test install/";
    private static final Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        logger.log(Level.INFO, "Main Thread - Start");

        Daemon.start();

        logger.log(Level.INFO, "Main Thread - Exit");
    }
}
