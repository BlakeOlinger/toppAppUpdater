package com.practice;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DaemonTest {
    private static Path tempSource;
    private static Path sameSource;
    private static Path tempTarget;
    private static Path sameTarget;

    @BeforeAll
    static void setUp() throws IOException {
        tempSource = Files.createFile(Paths.get("tempSource.txt"));
        tempTarget = Files.createFile(Paths.get("tempTarget.txt"));
        sameSource = Files.createFile(Paths.get("sameSource.txt"));
        sameTarget = Files.createFile(Paths.get("sameTarget.txt"));

        Files.writeString(tempSource, "foo bar");
        Files.writeString(tempTarget, "bar foo");
        Files.writeString(sameSource, "foo bar");
        Files.writeString(sameTarget, "foo bar");
    }

    @AfterAll
    static void tearDown() throws IOException {
        if(Files.exists(tempSource))
            Files.delete(tempSource);

        if (Files.exists(tempTarget))
            Files.delete(tempTarget);

        if(Files.exists(sameSource))
            Files.delete(sameSource);

        if(Files.exists(sameTarget))
            Files.delete(sameTarget);
    }

    @Test
    void return_true_for_two_files_one_name_diff() {
        var update = new Updates(
                tempSource,
                tempTarget
        );

        update.check();

        update.join();

        assertTrue(Config.areUpdates.contains(Boolean.TRUE));

        Config.areUpdates.clear();
    }

    @Test
    void return_false_for_two_files_not_diff() {
        var update = new Updates(
                sameSource,
                sameTarget
        );

        update.check();

        update.join();

        assertFalse(Config.areUpdates.contains(Boolean.TRUE));

        Config.areUpdates.clear();
    }
}