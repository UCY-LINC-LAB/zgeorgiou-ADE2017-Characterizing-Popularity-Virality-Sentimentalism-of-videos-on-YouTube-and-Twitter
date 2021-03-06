package com.zgeorg03.core;

import com.google.gson.JsonObject;
import com.zgeorg03.database.DBServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class DynamicInfoMonitor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(DynamicInfoMonitor.class);
    private  final DBServices dbServices;
    private final StatusMonitor statusMonitor;

    public DynamicInfoMonitor(DBServices dbServices, StatusMonitor statusMonitor) {
        this.dbServices = dbServices;
        this.statusMonitor = statusMonitor;
    }

    @Override
    public void run() {

        int count=1;
        while(true){
            List<String> videos = dbServices.getDbVideosService().getVideosThatNeedDynamicUpdate();

            if(videos.size()==0){
                try {
                    TimeUnit.MINUTES.sleep(count);
                    if(count<10)
                        count++;
                } catch (InterruptedException e) { e.printStackTrace(); }
            }else{
                count=1;
            }



            videos.forEach(video -> {
                //This is a new video
                String key = dbServices.getYouTubeAPIKey();
                if (!key.isEmpty()) {

                    YouTubeRequests requests = new YouTubeRequests(video, key);
                    String channelId = dbServices.getDbVideosService().getChannelId(video);
                    JsonObject dynamicData = requests.getDynamicData(channelId);

                    //Checking if a video reached 15th day
                    if (dbServices.getDbVideosService().checkVideoIsFinished(video)) {
                        if (dbServices.getDbVideosService().setVideoAsFinished(video))
                            logger.info("Video:" + video + " has finished!");
                        if (dbServices.getDbVideosService().addDynamicData(video, dynamicData))
                        logger.info("Dynamic data added for " + video);
                    }else if (dynamicData.get("error") != null) {
                        logger.error("Dynamic data couldn't be fetched for " + video + " because " + dynamicData.get("error").getAsString());
                        if (dbServices.getDbVideosService().setVideoAsIncomplete(video)) {
                            logger.error(video + " is set as incomplete");
                            statusMonitor.setReachedMonitorCapacity(false);
                        }
                    } else if (dbServices.getDbVideosService().addDynamicData(video, dynamicData))
                        logger.info("Dynamic data added for " + video);

                }else {
                    logger.error("YouTube key not available");
                }
            });
        }
    }
}
