package com.zgeorg03.services;

import com.google.gson.JsonObject;
import com.zgeorg03.core.StatusMonitor;
import com.zgeorg03.database.DBServices;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class IndexService extends Service {
    private final StatusMonitor monitor;

    public IndexService(DBServices dbServices, StatusMonitor monitor) {
        super(dbServices);
        this.monitor = monitor;
    }

    /**
     * Retrieve various stats for about collection process
     * @return
     */
    public JsonObject getStats(){
        JsonObject object = new JsonObject();
        String databaseName = dbServices.getDatabaseName();
        int youtubeKeys = dbServices.getTotalYouTubeAPIKeys();
        int twitterKeys = dbServices.getTotalTwitterApps();
        int finishedVideos = dbServices.getDbVideosService().getTotalFinishedVideos();
        int incompleteVideos = dbServices.getDbVideosService().getTotalIncompleteVideos();
        int maxVideosBeingMonitored = dbServices.getMaxVideosBeingMonitored();
        int maxCommentsPerVideo = dbServices.getMaxCommentsPerVideo();
        int monitoredVideos = dbServices.getDbVideosService().getTotalMonitoredVideosAndNotFinished();
        int totalTweets = dbServices.getTotalTweets();

        int videos_need_update = dbServices.getDbVideosService().getVideosThatNeedDynamicUpdate().size();

        JsonObject info = new JsonObject();
        info.addProperty("youtube_keys",youtubeKeys);
        info.addProperty("twitter_keys",twitterKeys);
        info.addProperty("videos_finished",finishedVideos);
        info.addProperty("videos_incomplete",incompleteVideos);
        info.addProperty("videos_monitored",monitoredVideos);
        info.addProperty("videos_need_update",videos_need_update);
        info.addProperty("total_tweets",totalTweets);

        JsonObject configurations = new JsonObject();
        info.addProperty("database_name",databaseName);
        configurations.addProperty("max_videos_being_monitored",maxVideosBeingMonitored);
        configurations.addProperty("max_comments_per_video",maxCommentsPerVideo);

        JsonObject currentSession = new JsonObject();
        currentSession.addProperty("duration",monitor.getDuration()/1000);
        currentSession.addProperty("videos_added",monitor.getVideosAdded());
        currentSession.addProperty("tweets_added",monitor.getTweetsAdded());

        object.add("info",info);
        object.add("configurations",configurations);
        object.add("current_session",currentSession);


        return object;

    }

    public JsonObject createApp(String name, String consumer_key,String consumer_secret,String token,String token_secret){
        JsonObject result = new JsonObject();
        boolean res = dbServices.addTwitterApp(name,consumer_key,consumer_secret,token,token_secret);

        if(res)
            result.addProperty("msg","Twitter App: "+name+" has been added");
        else
            result.addProperty("error","App: " + name+" already exists!");
        return result;
    }

    public JsonObject createYouTubeApp(String api_key){
        JsonObject result = new JsonObject();
        boolean res = dbServices.addYouTubeAPIKey(api_key);

        if(res)
            result.addProperty("msg","YouTube App: "+api_key+" has been added");
        else
            result.addProperty("error","API key: " + api_key+" already exists!");
        return result;
    }


    public JsonObject setMaxVideosBeingMonitored(int videos){
        JsonObject result = new JsonObject();
        if( dbServices.setMaxVideosBeingMonitored(videos)) {
            result.addProperty("msg", "Max videos being monitored is now: " + videos);
            monitor.setReachedMonitorCapacity(false);
        }
        else
            result.addProperty("error","Failed to set max videos being monitored");
        return result;
    }

    /**
     * TODO Need implementation I think
     * @param comments
     * @return
     */
    public JsonObject setMaxComments(int comments){
        JsonObject result = new JsonObject();
        if( dbServices.setMaxCommentsPerVideo(comments)) {
            result.addProperty("msg", "Max comments to collect for each video is now: " + comments);
        }
        else
            result.addProperty("error","Failed to set max comments per video");
        return result;
    }
}

