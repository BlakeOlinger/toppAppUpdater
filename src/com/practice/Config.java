package com.practice;

import java.io.FileInputStream;
import java.io.IOException;

class Config implements Runnable{
    private final Thread thread;
    private static String programState = "0";
    private static boolean isDatabaseInitalized = false;
    private static boolean updaterConfigInitalized = false;
    // TODO - make database output a second digit for its config file - 1 or 0 for not or is a Database
    // this daemon won't attempt to read from it until it reads a 0 on the second character of that config

    Config() {
        thread = new Thread(this, "Monitor Program State");
    }

    void monitorProgramState() {
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
        var programStatePath = "programFiles/config/updater.config";
        int readByte;
        var index = 0;
        System.out.println(" Live Update Daemon - Start");
        do {
            System.out.println(" Reading updater.config");
            try (var updaterConfig = new FileInputStream(programStatePath)){
                do {
                    readByte = updaterConfig.read();
                    for(;index < 1; ++index) {
                        programState = String.valueOf((char) readByte);
                    }

                    System.out.println(" Current Live Update State - " + programState);

                } while (readByte != -1);
            } catch (IOException ignore) {
                System.out.println(" ERROR: Could Not Read Config File");
            }
            index = 0;
            try {
                System.out.println(" Live Update Thread Sleep - 2,000 ms");
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        } while (programState.compareTo("0") == 0);

        System.out.println(" Live Update Daemon - End");
    }
}
