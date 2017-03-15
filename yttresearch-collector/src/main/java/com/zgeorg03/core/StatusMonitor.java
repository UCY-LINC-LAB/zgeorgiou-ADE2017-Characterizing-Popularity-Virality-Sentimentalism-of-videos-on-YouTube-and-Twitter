package com.zgeorg03.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.utils.YoutubeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class StatusMonitor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(StatusMonitor.class);
    private final Queue<Status> statusQueue = new ConcurrentLinkedDeque<>();
    private final DBServices dbServices;
    private final VideoStreamListener videoStreamListener;
    private final TwitterStream twitterStream;

    private final Map<String,Boolean> apps = new HashMap<>();

    private  int tweetsAdded;
    private  int videosAdded;

    private boolean reachedMonitorCapacity;
    private long startedTime;

    public StatusMonitor(DBServices dbServices) {
        this.dbServices = dbServices;
        this.videoStreamListener = new VideoStreamListener(this);
        twitterStream = new TwitterStreamFactory().getInstance();
    }
    @Override
    public void run() {
        JsonObject app;
        while(true){
            app = dbServices.getTwitterAppForUse();
            if(app.get("error")!=null) {
                try {
                    logger.info("Twitter apps not found! Sleeping for 30 seconds...");
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                break;
            }
        }



        String name = app.get("name").getAsString();
        String consumer_key = app.get("consumer_key").getAsString();
        String consumer_secret = app.get("consumer_secret").getAsString();
        String token = app.get("token").getAsString();
        String token_secret = app.get("token_secret").getAsString();
        apps.put(name,true);
        twitterStream.setOAuthConsumer(consumer_key, consumer_secret);
        twitterStream.setOAuthAccessToken(new AccessToken(token,token_secret));
        twitterStream.addListener(videoStreamListener);
        twitterStream.filter("youtube");

        startedTime = System.currentTimeMillis();
        long sleepTime=100;
        while(true){
            Status status;
            status = statusQueue.poll();
            if(status==null) {
                try { TimeUnit.MILLISECONDS.sleep(sleepTime); } catch (InterruptedException e) { e.printStackTrace();}
                continue;
            }

            URLEntity urls[] = status.getURLEntities();

            /**TODO Fix  Favorites count
            Status status = twitter.showStatus(Long.parseLong(tweetID));
            int favorites = (status.isRetweet()) ?
                    status.getRetweetedStatus().getFavoriteCount() :
                    status.getFavoriteCount();**/

            // No urls found
            if (urls.length == 0)
                continue;

            for (URLEntity url : urls) {
                String expandedUrl = url.getExpandedURL();
                //Make sure that is a youtube link
                if(!YoutubeTool.applyYoutubeFilter(expandedUrl))
                    continue;
                String videoId = YoutubeTool.extractID(expandedUrl);

                JsonObject tweet = new JsonObject();
                tweet.addProperty("video_id", videoId);
                tweet.addProperty("created_at", status.getCreatedAt().getTime());
                tweet.addProperty("text",status.getText());
                tweet.addProperty("favorite_count",status.getFavoriteCount());
                tweet.addProperty("lang",status.getLang());
                tweet.addProperty("retweet_count",status.getRetweetCount());
                tweet.addProperty("is_favorited",status.isFavorited());
                tweet.addProperty("is_possibly_sensitive",status.isPossiblySensitive());
                tweet.addProperty("is_retweet",status.isRetweet());
                User user = status.getUser();
                tweet.addProperty("user_created_at",user.getCreatedAt().getTime());
                tweet.addProperty("user_followers_count",user.getFollowersCount());
                tweet.addProperty("user_friends_count",user.getFriendsCount());
                tweet.addProperty("user_favorites_count",user.getFavouritesCount());
                tweet.addProperty("user_listed_count",user.getListedCount());
                tweet.addProperty("user_statuses_count",user.getStatusesCount());
                tweet.addProperty("user_verified",user.isVerified());
                tweet.addProperty("user_lang",user.getLang());
                JsonArray hashtags = new JsonArray();
                for(HashtagEntity hashtagEntity :status.getHashtagEntities())
                    hashtags.add(hashtagEntity.getText());
                tweet.add("hashtags",hashtags);

                long id = status.getId();

                // When a video is in the database
                if(dbServices.getDbVideosService().checkVideoExistenceOnly(videoId)){
                    //When it's also monitored we need to add its tweet
                    if(dbServices.getDbVideosService().checkVideoExistenceAndBeingMonitored(videoId)){

                        boolean added =  dbServices.addTweet(id,tweet);
                        if(added) {
                            //logger.info("Tweet added for video: "+videoId);
                            tweetsAdded++;
                        }

                    }

                    //If we have space for more videos to monitor
                }else if(!reachedMonitorCapacity){

                    if(dbServices.getDbVideosService().getTotalMonitoredVideosAndNotFinished()>=dbServices.getMaxVideosBeingMonitored()) {
                        reachedMonitorCapacity=true;
                        logger.info("Reached Monitoring videos capacity");
                        continue;
                    }

                    //This is a new video
                    String key = dbServices.getYouTubeAPIKey();
                    if(key.isEmpty()){
                        logger.error("YouTube key not available");
                        continue;
                    }

                    YouTubeRequests requests = new YouTubeRequests(videoId,key);
                    JsonObject videoData = requests.getStaticData();
                    if(videoData.get("error")!=null) {
                        logger.info("Cannot add Video: " + videoId + " because of " + videoData.get("error").getAsString());
                        continue;
                    }
                    boolean videoAdded = dbServices.getDbVideosService().addNewVideo(videoId,videoData);

                    if(videoAdded){
                        boolean added =  dbServices.addTweet(id,tweet);
                        if(added) {
                            //logger.info("New video & tweet added: "+videoId);
                            tweetsAdded++;
                            videosAdded++;
                        }

                    }else{
                        logger.info("Video: " + videoId +" couldn't be added!");

                    }
                }
            }


        }

    }

    public void shutdown(){
        logger.info("Status monitor shutting down...");
        for(String name : apps.keySet()){
            //dbServices.setVideosFound(videosAdded,videosWithLocationFound);
            dbServices.releaseTwitterApp(name);
        }
    }

    public void  enqueue(Status status) {
            statusQueue.add(status);
    }

    public int getQueueSize(){
        return statusQueue.size();
    }

    public void setReachedMonitorCapacity(boolean reachedMonitorCapacity) {
        this.reachedMonitorCapacity = reachedMonitorCapacity;
    }

    public int getVideosAdded() {
        return videosAdded;
    }

    public int getTweetsAdded() {
        return tweetsAdded;
    }

    /**
     * Get Status duration
     * @return
     */
   public long getDuration(){
        if(startedTime==0)
            return 0;
        return System.currentTimeMillis()-startedTime;
    }
}
