package ru.otus.message_system;

import ru.otus.message_system.message_server.MessageServer;
import ru.otus.message_system.process_runner.ProcessRunnerImpl;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MsgSystemMain {
    private static final String LOG_CONFIG_PATH = "./message-system/logging.properties";
    private static final Logger logger = Logger.getLogger(MsgSystemMain.class.getName());

    private static final String DB_SERVICE_EXE = "./db-service/target/db-service.jar";
    private static final String MASTER_DB_SERVICE_FLAG = "-f";
    private static final int DB_SERVICES_NUM = 2;
    private static final int DB_SERVICE_START_DELAY_SEC = 5;

    public static void main(String[] args) throws Exception {
        LogManager.getLogManager().readConfiguration(new FileInputStream(LOG_CONFIG_PATH));

        new MsgSystemMain().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        startDBServices(executorService);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=MessageServer");
        MessageServer server = new MessageServer();
        mbs.registerMBean(server, name);

        server.start();

        executorService.shutdown();
    }

    private void startDBServices(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start("java -jar " +
                                                DB_SERVICE_EXE + " " +
                                                MASTER_DB_SERVICE_FLAG);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, DB_SERVICE_START_DELAY_SEC, TimeUnit.SECONDS);

        for (int i = 1; i <= DB_SERVICES_NUM - 1; i++) {
            executorService.schedule(() -> {
                try {
                    new ProcessRunnerImpl().start("java -jar " +
                                                        DB_SERVICE_EXE);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }, DB_SERVICE_START_DELAY_SEC + i * 5, TimeUnit.SECONDS);
        }
    }
}
