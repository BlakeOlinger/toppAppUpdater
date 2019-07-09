package com.practice;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class LiveUpdateTest {
    @Test
    void name_for_0_should_be_toppApp() {
        var updateIndex = 0;
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals("toppApp.jar", Config.UPDATE_NAME);
    }

    @Test
    void name_for_3_should_be_toppAppUpdater() {
        var updateIndex = 3;
        var expected = "toppAppUpdater.jar";
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals(expected, Config.UPDATE_NAME);
    }

    @Test
    void name_for_4_shoud_be_hostfxr() {
        var updateIndex = 4;
        var expected = "hostfxr.dll";
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals(expected, Config.UPDATE_NAME);
    }

    @Test
    void name_for_13_should_be_runtimeconfigJson() {
        var updateIndex = 13;
        var expected = "sw-part-auto-test.runtimeconfig.json";
        var liveUpdate = new LiveUpdate(updateIndex);

        liveUpdate.update();

        assertEquals(expected, Config.UPDATE_NAME);
    }
}