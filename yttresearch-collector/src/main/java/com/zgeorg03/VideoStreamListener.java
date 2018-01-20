package com.zgeorg03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * Created by zgeorg03 on 2/24/17.
 */
public class VideoStreamListener implements StatusListener {
    private static final Logger logger = LoggerFactory.getLogger(VideoStreamListener.class);
    private final StatusMonitor monitor;
    private int limitbefore;

    public VideoStreamListener(StatusMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void onStatus(Status status) {
        monitor.enqueue(status);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

        logger.info("DEL:"+statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

        int limit = numberOfLimitedStatuses;
        if(Math.abs(limit -limitbefore)>200){
            logger.info("LIMIT:"+numberOfLimitedStatuses);
        }
        limitbefore=numberOfLimitedStatuses;
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        logger.info("SCRUB:"+userId);
    }

    @Override
    public void onStallWarning(StallWarning warning) {

        logger.info("STALL:"+warning.getMessage());
    }

    @Override
    public void onException(Exception ex) {
        logger.error(ex.getLocalizedMessage());

    }
}
