package com.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class Daemon {
    private static final Path CONFIG_PATH = Paths.get(Main.userRoot + "programFiles/config/updater.config");
    private static final Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static int countDown = 3;

    static void start(){
        logger.log(Level.INFO, "Daemon - Start");

       var initialize = new Initialize();

       initialize.start();

       initialize.join();


        do {
            checkProgramState();

            if (countDown-- == 0) {
                logger.log(Level.INFO, "Daemon - Check for Updates - Start");

                var updates = new ArrayList<Updates>();

                for (String name : Config.MICROSERVICE_NAMES) {
                    updates.add(
                            new Updates(
                                    Paths.get(Config.SOURCE_ROOT + name),
                                    Paths.get(Config.TARGET_ROOT + name)
                            )
                    );
                }

                for (String name : Config.SW_BIN_NAMES) {
                    updates.add(
                            new Updates(
                                    Paths.get(Config.SW_SOURCE_ROOT + name),
                                    Paths.get(Config.SW_TARGET_ROOT + name)
                            )
                    );
                }

                for(Updates update : updates) {
                    update.check();

                    update.join();
                }

                countDown = 3;

//                System.out.println("Config.areUpdates - size - " + Config.areUpdates.size());

                if (Config.areUpdates.contains(Boolean.TRUE)) {
                    logger.log(Level.INFO,"Daemon - Check for Updates - Updates Detected");

                    logger.log(Level.INFO, "Daemon - Check for Updates - Applying Updates - Start");

                    var liveUpdates = new ArrayList<LiveUpdate>();

//                    System.out.println("max index length - "
//                            + (Config.MICROSERVICE_NAMES.length
//                            + Config.SW_BIN_NAMES.length));

                    for(var i = 0; i < Config.MICROSERVICE_NAMES.length
                            + Config.SW_BIN_NAMES.length; ++i) {
                        if (Config.areUpdates.get(i)) {
                            liveUpdates.add(
                                  new LiveUpdate(i)
                           );
//                            Config.areUpdates.forEach(System.out::println);
//                            System.out.println("index = " + i);

                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ignore) {
                            }
                        }
                    }

                    liveUpdates.forEach(LiveUpdate::update);

                    liveUpdates.forEach(LiveUpdate::join);

                    logger.log(Level.INFO, "Daemon - Check for Updates - Applying Updates - End");

                    Config.areUpdates.clear();
//                    System.out.println("Config are updates size - expect - 0 - " + Config.areUpdates.size());
                } else {
                    logger.log(Level.INFO, "Daemon - Check for Updates - No Updates Detected");

                    Config.areUpdates.clear();
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        } while (Config.programState.compareTo("0") == 0);

        logger.log(Level.INFO, "Daemon - Exit");
    }

    private static void checkProgramState() {
        try {
            Config.programState = Files.readString(CONFIG_PATH).substring(0,1);
        } catch (IOException ignore) {
        }
    }
}
