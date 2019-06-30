package com.practice;

import java.io.FileInputStream;
import java.io.IOException;

public class Config implements Runnable{
    private final Thread thread;
    static String programState = "0";

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
        var programStatePath = "programFiles/config/DBdaemon.config";
        int readByte;
        var index = 0;
        System.out.println(" Database Daemon - Start");
        do {
            System.out.println(" Reading Database.config...");
                try (var DBconfig = new FileInputStream(programStatePath)){
                    do {
                        readByte = DBconfig.read();
                      for(;index < 1; ++index) {
                          programState = String.valueOf((char) readByte);
                      }

                        System.out.println(" Database Program State - " + programState);
                    } while (readByte != -1);
                } catch (IOException ignore) {

                }
                index = 0;
            try {

                System.out.println(" Database Thread Sleep - 2,000 ms");
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        } while (programState.compareTo("0") == 0);

        System.out.println(" Database Daemon - End");
    }
}
