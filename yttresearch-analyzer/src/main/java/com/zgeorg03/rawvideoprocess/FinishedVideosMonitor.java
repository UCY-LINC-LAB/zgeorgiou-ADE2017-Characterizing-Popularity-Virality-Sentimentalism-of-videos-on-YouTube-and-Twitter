package com.zgeorg03.rawvideoprocess;

import com.zgeorg03.database.DBServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 3/4/17.
 */
public class FinishedVideosMonitor implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(FinishedVideosMonitor.class);
    private final DBServices dbServices;
    private int maxVideos;

    public FinishedVideosMonitor(DBServices dbServices,int maxVideos) {
        this.dbServices = dbServices;
        this.maxVideos = maxVideos;
    }


    @Override
    public void run() {
        logger.info("Finished Videos Monitor started...");
        while(true) {
            List<String> notProcessed = dbServices.getFinishedButNotProcessedVideos(maxVideos);

            if (notProcessed.isEmpty()) {
                try { TimeUnit.HOURS.sleep(6); } catch (InterruptedException e) { }
            }

            notProcessed.forEach(videoId -> {
                try {
                    ProcessVideo processVideo = new ProcessVideo(dbServices, videoId);
                    RawVideo rawVideo = processVideo.getVideo();
                    dbServices.getProcessRawVideoDBService().addOrReplaceProcessedVideo(rawVideo);
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            });

            try { TimeUnit.MINUTES.sleep(1); } catch (InterruptedException e) { }
        }
    }
}
