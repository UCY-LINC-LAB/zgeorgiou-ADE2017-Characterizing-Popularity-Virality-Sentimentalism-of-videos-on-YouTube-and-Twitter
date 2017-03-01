package com.zgeorg03.core;

import com.google.gson.JsonObject;
import com.zgeorg03.controllers.helpers.Parameter;
import com.zgeorg03.database.DBServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class IncompleteVideosMonitor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(IncompleteVideosMonitor.class);
    private  final DBServices dbServices;

    public IncompleteVideosMonitor(DBServices dbServices){
        this.dbServices = dbServices;
    }

    @Override
    public void run() {

        while(true){
            List<String> videos = dbServices.getDbVideosService().getVideosThatAreIncomplete();

            if(videos.size()==0){
                try {
                    TimeUnit.HOURS.sleep(1); // 1 hours
                } catch (InterruptedException e) { e.printStackTrace(); }
            }

            videos.forEach(video ->{
                try {
                    int comments = dbServices.deleteComments(video);
                    int tweets = dbServices.deleteTweets(video);
                    boolean deleted = dbServices.getDbVideosService().deleteVideo(video);
                    if(deleted)
                        logger.info("Video:"+video+" has been removed with "+tweets+" tweets, and "+comments+" comments");
                    else
                        logger.error("Failed to delete video:" +video);
                }catch (Exception e) {
                    logger.error("Failed to delete: " + video);
                }

            });

        }
    }
}
