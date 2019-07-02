package com.practice;

import java.io.*;

class LiveUpdate implements Runnable{
    private final Thread thread;

    LiveUpdate() {
        thread = new Thread(this, "Live Update");
    }

    void checkAndUpdate() {
        thread.start();
    }

    @Override
    public void run() {

        var updater = "toppAppUpdater.jar";


        var configIndex = 0;

        for (String file :
                files) {

            if(whereDiff(latestVersionPath + file,
                    currentVersionPath + file)) {
                message = " Found New " + file + " Version - Updating...";
                FileLog.logFile(message);

                var configPath = "programFiles/config/";
                var name = configNames[configIndex++];

                update(
                        name,
                        configPath,
                        latestVersionPath,
                        file,
                        currentVersionPath
                );

                if(!whereDiff(latestVersionPath + file,
                        currentVersionPath + file)) {
                    message = " " + file + " Update Success";
                    FileLog.logFile(message);
                } else {
                    message = " ERROR: Update Failed";
                    FileLog.logFile(message);
                }


            } else {
                message = " Microservice " + file + " Up To Date";
                FileLog.logFile(message);
            }
        }

        if(whereDiff(currentVersionPath + updater,
                latestVersionPath + updater)) {
            message = " Live Update - New Version Found - Updating Live Update Microservice";
            FileLog.logFile(message);

            updateLiveUpdateMicroservice();
        } else {
            message = " Live Update - Up To Date";
            FileLog.logFile(message);
        }

        message = " Live Update - Check and Update - End";
        FileLog.logFile(message);

    }

    private void updateLiveUpdateMicroservice() {
        var message = " Live Update - Sending Update Live Update Command to Master";
        FileLog.logFile(message);

        var masterConfigPath = "programFiles/config/master.config";

        try (var masterConfig = new FileOutputStream(masterConfigPath)) {
            var commandOne = (int) '0';
            var updateCommand = (int) '0';

            masterConfig.write(commandOne);
            masterConfig.write(updateCommand);

        } catch (IOException ignore) {
            message = " ERROR: Could Not Send Command to Master";
            FileLog.logFile(message);
        }
    }

    private boolean whereDiff(String currentPath, String latestPath) {
        try (var currentFile = new BufferedInputStream(
                new FileInputStream(currentPath)
        );
        var latestFile = new BufferedInputStream(
                new FileInputStream(latestPath)
        )) {
            var message = "";
            int byteReadCurrent = 0, byteReadLatest = 0, position = 1;

            do {
                if(byteReadCurrent != byteReadLatest) {
                    message = " Files different at Position - " + position;
                    FileLog.logFile(message);
                    return true;
                }

                ++position;

                byteReadCurrent = currentFile.read();
                byteReadLatest = latestFile.read();

            } while (byteReadCurrent != -1 && byteReadLatest != -1);

            if (byteReadCurrent != byteReadLatest) {
                message = " Files Diff in Length";
                FileLog.logFile(message);
                return true;
            } else {
                message = " Files Identical";
                FileLog.logFile(message);

                return false;
            }

        } catch (IOException ignore) {
            return false;
        }
    }


    private void update(String configFileName, String configPath,
                        String latestPath, String currentFileName,
                        String currentPath) {

        sendKillCommand(configFileName,configPath);


        byte[] latestVersionBytes = readFile(latestPath + currentFileName);

        if( latestVersionBytes != null) {
            writeBytesToFile(latestVersionBytes, currentFileName, currentPath);
            writeBytesToFile(latestVersionBytes, currentFileName, "");
        }




        sendStartCommand(configFileName, configPath, currentFileName);


    }

    private void sendStartCommand(String configFileName, String configPath, String currentFileName) {
        int command = (int) '0';

        var message = " Sending Start Command to " + configFileName +
                " and " + currentFileName;
        FileLog.logFile(message);


        writeIntsToFile(command, configFileName, configPath);

        try {
            Runtime.getRuntime().exec(" cmd.exe /c " + currentFileName);
        } catch (IOException ignore) {
            message = " ERROR: Cannot Start " + currentFileName;
            FileLog.logFile(message);
        }

    }

    private void sendKillCommand(String configFileName, String configPath) {
        int command = (int) '1';

        var message = " Sending Kill Command to " + configFileName;
        FileLog.logFile(message);

        writeIntsToFile(command, configFileName, configPath);
    }

    private void writeIntsToFile(int command, String configFileName, String configPath) {
        var message = " Writing Ints to File " + configFileName;
        FileLog.logFile(message);

        try (var writeFile = new FileOutputStream(configPath + configFileName)) {

            writeFile.write(command);

            if(configFileName.compareTo("toppAppDBdaemon.jar") == 0 ||
            configFileName.compareTo("toppAppMaster.jar") == 0) {
                var commandTwo = (int) '1';
                writeFile.write(commandTwo);
            }
        } catch (IOException ignore) {
            message = " Could Not Write Ints to File " + configFileName;
            FileLog.logFile(message);
        }
    }


    private void writeBytesToFile(byte[] inputs, String fileName, String path) {
        var message = " Writing to file " + path + fileName;
        FileLog.logFile(message);

        try (var writeFile = new FileOutputStream(path + fileName)) {
            for (byte element :
                    inputs) {
                writeFile.write(element);
            }

            message = " File Write Success";
            FileLog.logFile(message);

        } catch (IOException ignore) {
            message = " ERROR: Could Not Write to File " + path + fileName;
            FileLog.logFile(message);
        }
    }

    private byte[] readFile(String filePath) {

        var message = " Reading File - " + filePath;
        FileLog.logFile(message);

        try (var file = new FileInputStream(filePath)) {
            var bytes = new byte[file.available()];

                bytes = file.readAllBytes();


            message = " File Successfully Read - " + filePath;
            FileLog.logFile(message);

            return bytes;

        } catch (IOException ignore) {
            message = " File Not Found";
            FileLog.logFile(message);

            return null;
        }
    }
}
