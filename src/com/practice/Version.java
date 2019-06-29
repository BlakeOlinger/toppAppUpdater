package com.practice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Version {
    static void copyLatestToCurrent(String latestPath, String currentPath) {
        try (var latest = new FileInputStream(latestPath);
        var current = new FileOutputStream(currentPath)){
            int byteRead;
            do {
                byteRead = latest.read();
                if(byteRead != -1)
                    current.write(byteRead);
            } while (byteRead != -1);
        } catch (IOException ignore) {
        }
    }
}
