package com.practice;

import java.io.IOException;

public class PullDaemon implements Runnable{
    private final Thread thread;

    PullDaemon() {
        thread = new Thread(this, "Pull Daemon");
    }

    void start() {
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

        do {


            // add a GUI window indicating status - near future
            try {
               //  var sleepIntervalMS = 600_000;
                var testSleepIntervalMS = 10_000;

                // fully works - can't have any untracked files in local repo though
                var process = Runtime.getRuntime().exec("cmd.exe /c cd toppAppDBdaemon/ && git pull origin master");

                process.waitFor();

                // System.out.println("End Update");

                Thread.sleep(testSleepIntervalMS);
            } catch (InterruptedException | IOException ignore) {

            }

            System.out.println(Config.programState.compareTo("0"));
        } while(Config.programState.compareTo("0") == 0);
    }
}
