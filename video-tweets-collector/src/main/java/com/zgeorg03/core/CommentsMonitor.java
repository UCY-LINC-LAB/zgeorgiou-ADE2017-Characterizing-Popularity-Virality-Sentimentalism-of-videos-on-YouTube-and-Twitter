package com.zgeorg03.core;

import com.google.gson.JsonObject;
import com.zgeorg03.database.DBServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class CommentsMonitor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(CommentsMonitor.class);
    private  final DBServices dbServices;
    private  final int maxComments;

    public CommentsMonitor(DBServices dbServices, int maxComments) {
        this.dbServices = dbServices;
        this.maxComments = maxComments;
    }

    @Override
    public void run() {

        int count=1;
        while(true){
            Map<String,Integer> videos = dbServices.getDbVideosService().getVideosThatNeedComments(maxComments);

            if(videos.size()==0){
                try {
                    logger.info("Comments Monitor: Sleeping for "+ count+" minutes");
                    TimeUnit.MINUTES.sleep(1*count);
                    if(count<60)
                        count*=2;
                } catch (InterruptedException e) { e.printStackTrace(); }
            }else{
                count=1;
            }


            //This is a new video
            String key = dbServices.getYouTubeAPIKey();
            if(key.isEmpty()){
                logger.error("YouTube key not available");
                continue;
            }

            videos.entrySet().forEach(entry ->{
                String video = entry.getKey();
                int max = entry.getValue();
                YouTubeRequests requests = new YouTubeRequests(video,key);
                JsonObject commentsData =  requests.getLatestComments(max);
                int added =dbServices.addComments(video,commentsData);
                logger.info((video+": added " + added +" of "+max+" comments"));
            });
        }
    }
}
