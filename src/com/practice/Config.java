package com.practice;

import java.io.FileInputStream;
import java.io.IOException;

class Config implements Runnable{
    private final Thread thread;
    private static String programState = "0";
    static boolean isDatabaseInitialized = false;

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
        var logMessage = "";

        logMessage = " Live Update Daemon - Start";
        FileLog.logFile(logMessage);

        if(isDatabaseInitialized) {
            var programStatePath = "programFiles/config/updater.config";
            int readByte;
            var index = 0;


            do {
                logMessage = " Reading updater.config";
                FileLog.logFile(logMessage);

                try (var updaterConfig = new FileInputStream(programStatePath)) {
                    do {
                        readByte = updaterConfig.read();
                        for (; index < 1; ++index) {
                            programState = String.valueOf((char) readByte);
                        }

                        logMessage = " Current Live Update State - " + programState;
                        FileLog.logFile(logMessage);

                    } while (readByte != -1);
                } catch (IOException ignore) {
                    logMessage = " ERROR: Could Not Read Config File";
                    FileLog.logFile(logMessage);
                }
                index = 0;

                new LiveUpdate().checkAndUpdate();

                try {
                    logMessage = " Live Update Thread Sleep - 2,000 ms";
                    FileLog.logFile(logMessage);

                    Thread.sleep(2000);
                } catch (InterruptedException ignore) {
                }
            } while (programState.compareTo("0") == 0);
        }

        logMessage = " Live Update Daemon - End";
        FileLog.logFile(logMessage);
    }
}
