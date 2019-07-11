package com.practice;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class LiveUpdateTest {
    @Test
    void name_for_0_should_be_toppApp() {
        var updateIndex = 0;
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals("toppApp.jar", liveUpdate.getUpdateName());
    }

    @Test
    void name_for_3_should_be_toppAppUpdater() {
        var updateIndex = 3;
        var expected = "toppAppUpdater.jar";
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals(expected, liveUpdate.getUpdateName());
    }

    @Test
    void name_for_4_should_be_hostfxr() {
        var updateIndex = 4;
        var expected = "hostfxr.dll";
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals(expected, liveUpdate.getUpdateName());
    }

    @Test
    void name_for_13_should_be_runtimeconfigJson() {
        var updateIndex = 13;
        var expected = "sw-part-auto-test.runtimeconfig.json";
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals(expected, liveUpdate.getUpdateName());
    }

    @Test
    void return_toppAppBat_for_toppAppJar() {
        // LiveUpdate(0) -> Config.UPDATE_NAME = "toppApp.jar";
        var liveUpdate = new LiveUpdate(0);
        var expected = "toppApp.bat";

        liveUpdate.getBatName();

        assertEquals(expected, Config.START_BAT_NAME);
    }

    @Test
    void return_toppAppUpdaterBat_for_toppAppUpdaterJar() {
        // LiveUpdate(3) -> Config.UPDATE_NAME = "toppAppUpdater.jar";
        var liveUpdate = new LiveUpdate(3);
        var expected = "toppAppUpdater.bat";

        liveUpdate.getBatName();

        assertEquals(expected, Config.START_BAT_NAME);
    }

    @Test
    void return_sw_part_auto_test_bat_for_similar_jar() {
        // LiveUpdate(<4+>) -> Config.UPDATE_NAME = "sw-part-auto-test.jar";
        var liveUpdate = new LiveUpdate(4);
        var expected = "sw-part-auto-test.bat";

        liveUpdate.getBatName();

        assertEquals(expected, Config.START_BAT_NAME);
    }
}