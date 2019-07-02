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

public class Main {

    public static void main(String[] args) {
        Daemon.start();
        /*
        try {
            var initialize = new Initialize();

            initialize.thread.start();
            initialize.thread.join();

            new Config().monitorProgramState();
        } catch (InterruptedException ignore) {
        }

         */
    }
}
