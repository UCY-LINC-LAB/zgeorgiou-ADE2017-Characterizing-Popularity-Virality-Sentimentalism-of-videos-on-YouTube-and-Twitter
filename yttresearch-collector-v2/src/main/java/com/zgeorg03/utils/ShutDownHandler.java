package com.zgeorg03.utils;

import com.zgeorg03.StatusMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class ShutDownHandler extends Thread{
    private final Logger logger = LoggerFactory.getLogger(ShutDownHandler.class);

    private final StatusMonitor statusMonitor;

    public ShutDownHandler(StatusMonitor statusMonitor) {
        this.statusMonitor = statusMonitor;
    }

    @Override
    public void run() {
        logger.info("Shutting down...");
        statusMonitor.shutdown();
    }
}
