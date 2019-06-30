package com.practice;

import java.io.FileOutputStream;
import java.io.IOException;

class FileLog implements Runnable{
    private final Thread thread;
    private static String logMessage = "";

    private FileLog() {
        thread = new Thread(this, "File Logging");
    }

    private void logMessage() {
        // thread.start();
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
        if(logMessage.length() > 0) {
            try (var logFile = new FileOutputStream("LiveUpdate.log")) {
                char[] message = logMessage.toCharArray();

                for (char c : message) {
                    logFile.write((int) c);
                }

            } catch (IOException ignore) {

            }

        }

    }

    static void logFile(String message) {
        FileLog.logMessage += "\n" + message;

        System.out.println(message);
        new FileLog().logMessage();
    }
}
