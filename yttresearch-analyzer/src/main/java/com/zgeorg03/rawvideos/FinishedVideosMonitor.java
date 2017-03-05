package com.zgeorg03.rawvideos;

import com.zgeorg03.database.DBServices;
import com.zgeorg03.rawvideos.models.RawVideo;
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
                logger.info("All videos are processed. Going to sleep...");
                try { TimeUnit.HOURS.sleep(6); } catch (InterruptedException e) { }
            }

            notProcessed.forEach(videoId -> {
                try {
                    ProcessVideo processVideo = new ProcessVideo(dbServices, videoId);
                    RawVideo rawVideo = processVideo.getVideo();
                    dbServices.getProcessVideoDBService().addOrReplaceProcessedVideo(rawVideo);

                    //Normally we set it as processed
                    dbServices.getProcessVideoDBService().setVideoAsProcessed(videoId);
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            });

            try { TimeUnit.SECONDS.sleep(60); } catch (InterruptedException e) { }
        }
    }
}
