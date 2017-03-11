package com.zgeorg03.rawvideos;

import com.zgeorg03.analysis.SentimentAnalysis;
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

    private final SentimentAnalysis sentimentAnalysis;
    public FinishedVideosMonitor(DBServices dbServices, int maxVideos, SentimentAnalysis sentimentAnalysis) {
        this.dbServices = dbServices;
        this.maxVideos = maxVideos;
        this.sentimentAnalysis = sentimentAnalysis;
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
                    ProcessVideo processVideo = new ProcessVideo(dbServices, videoId, sentimentAnalysis);
                    RawVideo rawVideo = processVideo.getVideo();
                    dbServices.getProcessVideoDBService().addOrReplaceProcessedVideo(rawVideo);

                    //Normally we set it as processed
                    // TODO CHANGE THISSS IN PRODUCTION
                   dbServices.getProcessVideoDBService().setVideoAsProcessed(videoId);
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            });

            // TODO CHANGE THIS IN PRODUCTION
            try { TimeUnit.SECONDS.sleep(60); } catch (InterruptedException e) { }
        }
    }
}
